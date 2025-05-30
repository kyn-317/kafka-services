package com.kyn.order;


import com.kyn.common.messages.CartRequest;
import com.kyn.common.messages.Request;
import com.kyn.common.messages.inventory.InventoryRequest;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.messages.payment.PaymentRequest;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.order.common.dto.CartOrderDetails;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.dto.OrderCreateRequest;
import com.kyn.order.common.dto.OrderDetails;
import com.kyn.order.common.dto.PurchaseOrderDto;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@DirtiesContext
@AutoConfigureWebTestClient
@SpringBootTest(properties = {
		"logging.level.root=ERROR",
		"logging.level.com.kyn*=INFO",
		"spring.cloud.stream.kafka.binder.configuration.auto.offset.reset=earliest",
		"spring.cloud.function.definition=requestConsumer;orderOrchestrator",
		"spring.cloud.stream.bindings.requestConsumer-in-0.destination=cart-payment-request,cart-inventory-request"
})
@EmbeddedKafka(
		partitions = 1,
		bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
@Import(AbstractIntegrationTest.TestConfig.class)
public abstract class AbstractIntegrationTest {

	private static final Sinks.Many<CartRequest> resSink = Sinks.many().unicast().onBackpressureBuffer();
	private static final Flux<CartRequest> resFlux = resSink.asFlux().cache(0);

	@Autowired
	private WebTestClient client;

	@Autowired
	private StreamBridge streamBridge;

	protected void emitResponse(PaymentResponse response){
		this.streamBridge.send("cart-payment-response", response);
	}

	protected void emitResponse(InventoryResponse response){
		this.streamBridge.send("cart-inventory-response", response);
	}


	protected UUID initiateOrder(OrderCreateRequest request){
		var orderIdRef = new AtomicReference<UUID>();
		this.client
				.post()
				.uri("/order")
				.bodyValue(request)
				.exchange()
				.expectStatus().isAccepted()
				.expectBody()
				.jsonPath("$.orderId").exists()
				.jsonPath("$.orderId").value(id -> orderIdRef.set(UUID.fromString(id.toString())))
				.jsonPath("$.status").isEqualTo("PENDING");
		return orderIdRef.get();
	}

	
	protected UUID initiateCartOrder(OrderByCart request){
		var orderIdRef = new AtomicReference<UUID>();
		this.client
				.post()
				.uri("/order/byCart")
				.bodyValue(request)
				.exchange()
				.expectStatus().isAccepted()
				.expectBody()
				.jsonPath("$.orderId").exists()
				.jsonPath("$.orderId").value(id -> orderIdRef.set(UUID.fromString(id.toString())));
		return orderIdRef.get();
	}


	protected void verifyOrderDetails(UUID orderId){
		this.client
				.get()
				.uri("/order/{orderId}", orderId)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(CartOrderDetails.class)
				.value(r -> {
					Assertions.assertEquals(orderId, r.order().orderId());
					Assertions.assertNotNull(r.actions());
				});
	}

	protected void verifyOrderDetails(UUID orderId, Consumer<CartOrderDetails> assertions){
		this.client
				.get()
				.uri("/order/{orderId}", orderId)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(CartOrderDetails.class)
				.value(assertions);
	}

	protected void verifyAllOrders(UUID... orderIds){
		this.client
				.get()
				.uri("/order/all")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(new ParameterizedTypeReference<List<PurchaseOrderDto>>() {
				})
				.value(r -> {
					Assertions.assertEquals(List.of(orderIds), r.stream().map(PurchaseOrderDto::orderId).toList());
				});
	}

	protected void verifyPaymentRequest(UUID orderId, int amount){
		expectRequest(PaymentRequest.Process.class, e -> {
			Assertions.assertEquals(amount, e.amount());
			Assertions.assertEquals(orderId, e.orderId());
		});
	}

	protected void verifyInventoryRequest(UUID orderId, int quantity){
		expectRequest(InventoryRequest.Deduct.class, e -> {
			Assertions.assertEquals(quantity, e.quantity());
			Assertions.assertEquals(orderId, e.orderId());
		});
	}


	protected <T> void expectRequest(Class<T> type, Consumer<T> assertion){
		resFlux
				//.next()
				.timeout(Duration.ofSeconds(2), Mono.empty())
				.cast(type)
				.as(StepVerifier::create)
				.consumeNextWith(assertion)
				.verifyComplete();
	}

	protected void expectRequests(Consumer<List<Request>> assertion){
		resFlux
				//.next()
				.timeout(Duration.ofSeconds(2), Mono.empty())
				.cast(Request.class)
				.collectList()
				.as(StepVerifier::create)
				.consumeNextWith(assertion)
				.verifyComplete();
	}

	protected void expectNoRequest(){
		resFlux
				.next()
				.timeout(Duration.ofSeconds(2), Mono.empty())
				.as(StepVerifier::create)
				.verifyComplete();
	}

	@TestConfiguration
	static class TestConfig {

		@Bean
		public Consumer<Flux<CartRequest>> requestConsumer(){
			return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
		}

	}
}
