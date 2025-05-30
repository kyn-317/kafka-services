package com.kyn.order;
import com.kyn.common.messages.inventory.InventoryRequest;
import com.kyn.common.messages.inventory.InventoryResponse;
import com.kyn.common.messages.payment.PaymentRequest;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.order.common.enums.OrderStatus;
import com.kyn.order.common.enums.WorkflowAction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;
import com.kyn.common.dto.CartItem;
import com.kyn.order.common.dto.OrderByCart;
import com.kyn.order.common.dto.CartOrderDetails;
import java.util.function.Consumer;


public class OrderServiceTest extends AbstractIntegrationTest {

    @Test
    public void orderCompletedWorkflowTest() {
        // order create request
        List<CartItem> cartItems = List.of(
                new CartItem(UUID.randomUUID().toString(), "Test Product", 2.0, "image.png", 3)
        );
        OrderByCart request = TestDataUtil.toOrderByCart(UUID.randomUUID(), cartItems, "test@example.com");

        // validate order in pending state
        UUID orderId = this.initiateCartOrder(request);

        // check for payment request
        this.verifyPaymentRequest(orderId, 6);

        // emit payment processed response
        this.emitResponse(PaymentResponse.Processed.builder().orderId(orderId).build());

        // check for inventory request
        this.verifyInventoryRequest(orderId, 3);

        // emit inventory deducted response
        this.emitResponse(InventoryResponse.Deducted.builder().orderId(orderId).build());


        // check for no request
        this.expectNoRequest();

        this.verifyOrderDetails(orderId, (Consumer<CartOrderDetails>) r -> {
            Assertions.assertEquals(OrderStatus.COMPLETED, r.order().status());
            Assertions.assertFalse(r.actions().isEmpty());
            var expected = List.of(
                    WorkflowAction.PAYMENT_REQUEST_INITIATED,
                    WorkflowAction.PAYMENT_PROCESSED,
                    WorkflowAction.INVENTORY_REQUEST_INITIATED,
                    WorkflowAction.INVENTORY_DEDUCTED
            );
            Assertions.assertEquals(expected, r.actions().stream().map(dto -> dto.action()).toList());
        });

    }

    @Test
    public void orderCancelledWhenPaymentDeclinedTest() {
        // order create request
        List<CartItem> cartItems = List.of(
                new CartItem(UUID.randomUUID().toString(), "Test Product", 2.0, "image.png", 3)
        );
        OrderByCart request = TestDataUtil.toOrderByCart(UUID.randomUUID(), cartItems, "test@example.com");

        // validate order in pending state
        UUID orderId = this.initiateCartOrder(request);

        // check for payment request
        this.verifyPaymentRequest(orderId, 6);

        // emit payment declined response
        this.emitResponse(PaymentResponse.Declined.builder().orderId(orderId).build());

        // check for no request
        this.expectNoRequest();

        this.verifyOrderDetails(orderId, (Consumer<CartOrderDetails>) r -> {
            Assertions.assertEquals(OrderStatus.CANCELLED, r.order().status());
            Assertions.assertFalse(r.actions().isEmpty());
            var expected = List.of(
                    WorkflowAction.PAYMENT_REQUEST_INITIATED,
                    WorkflowAction.PAYMENT_DECLINED
            );
            Assertions.assertEquals(expected, r.actions().stream().map(dto -> dto.action()).toList());
        });

    }

    @Test
    public void orderCancelledWhenInventoryDeclinedTest() {
        // order create request
        List<CartItem> cartItems = List.of(
                new CartItem(UUID.randomUUID().toString(), "Test Product", 2.0, "image.png", 3)
        );
        OrderByCart request = TestDataUtil.toOrderByCart(UUID.randomUUID(), cartItems, "test@example.com");

        // validate order in pending state
        UUID orderId = this.initiateCartOrder(request);

        // check for payment request
        this.verifyPaymentRequest(orderId, 6);

        // emit payment processed response
        this.emitResponse(PaymentResponse.Processed.builder().orderId(orderId).build());

        // check for inventory request
        this.verifyInventoryRequest(orderId, 3);

        // emit inventory declined response
        this.emitResponse(InventoryResponse.Declined.builder().orderId(orderId).build());

        // check for refund request
        this.expectRequest(PaymentRequest.Refund.class, r -> Assertions.assertEquals(orderId, r.orderId()));

        this.verifyOrderDetails(orderId, (Consumer<CartOrderDetails>) r -> {
            Assertions.assertEquals(OrderStatus.CANCELLED, r.order().status());
            Assertions.assertFalse(r.actions().isEmpty());
            var expected = List.of(
                    WorkflowAction.PAYMENT_REQUEST_INITIATED,
                    WorkflowAction.PAYMENT_PROCESSED,
                    WorkflowAction.INVENTORY_REQUEST_INITIATED,
                    WorkflowAction.INVENTORY_DECLINED,
                    WorkflowAction.PAYMENT_REFUND_INITIATED
            );
            Assertions.assertEquals(expected, r.actions().stream().map(dto -> dto.action()).toList());
        });

    }

    @Test
    public void orderCancelledWhenShippingDeclinedTest() {
        // order create request
        List<CartItem> cartItems = List.of(
                new CartItem(UUID.randomUUID().toString(), "Test Product", 2.0, "image.png", 3)
        );
        OrderByCart request = TestDataUtil.toOrderByCart(UUID.randomUUID(), cartItems, "test@example.com");

        // validate order in pending state
        UUID orderId = this.initiateCartOrder(request);

        // check for payment request
        this.verifyPaymentRequest(orderId, 6);

        // emit payment processed response
        this.emitResponse(PaymentResponse.Processed.builder().orderId(orderId).build());

        // check for inventory request
        this.verifyInventoryRequest(orderId, 3);

        // emit inventory deducted response
        this.emitResponse(InventoryResponse.Deducted.builder().orderId(orderId).build());

        // check for refund and restore requests
        this.expectRequests(list -> {
            var refund = PaymentRequest.Refund.builder().orderId(orderId).build();
            var restore = InventoryRequest.Restore.builder().orderId(orderId).build();
            Assertions.assertEquals(List.of(restore, refund), list);
        });

        this.verifyOrderDetails(orderId, (Consumer<CartOrderDetails>) r -> {
            Assertions.assertEquals(OrderStatus.CANCELLED, r.order().status());
            Assertions.assertFalse(r.actions().isEmpty());
            var expected = List.of(
                    WorkflowAction.PAYMENT_REQUEST_INITIATED,
                    WorkflowAction.PAYMENT_PROCESSED,
                    WorkflowAction.INVENTORY_REQUEST_INITIATED,
                    WorkflowAction.INVENTORY_DEDUCTED,
                    WorkflowAction.INVENTORY_RESTORE_INITIATED,
                    WorkflowAction.PAYMENT_REFUND_INITIATED
            );
            Assertions.assertEquals(expected, r.actions().stream().map(dto -> dto.action()).toList());
        });

    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getAllOrdersTest(){
        // order create request
        List<CartItem> cartItems = List.of(
                new CartItem(UUID.randomUUID().toString(), "Test Product", 2.0, "image.png", 3)
        );
        OrderByCart request = TestDataUtil.toOrderByCart(UUID.randomUUID(), cartItems, "test@example.com");

        // validate order in pending state
        UUID orderId1 = this.initiateCartOrder(request);

        // check for payment request
        this.verifyPaymentRequest(orderId1, 6);

        // verify if GET all orders API returns one item in the response
        this.verifyAllOrders(orderId1);

        // place another order
        UUID orderId2 = this.initiateCartOrder(request);

        // check for payment request
        this.verifyPaymentRequest(orderId2, 6);

        // verify if GET all orders API returns 2 items in the response
        this.verifyAllOrders(orderId1, orderId2);

    }

}