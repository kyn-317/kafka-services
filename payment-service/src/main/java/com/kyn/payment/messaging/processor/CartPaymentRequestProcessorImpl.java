package com.kyn.payment.messaging.processor;

import java.util.function.UnaryOperator;

import org.springframework.stereotype.Service;

import com.kyn.common.exception.EventAlreadyProcessedException;
import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.payment.common.service.CartPaymentService;
import com.kyn.payment.messaging.mapper.CartMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartPaymentRequestProcessorImpl implements CartPaymentRequestProcessor {
    private final CartPaymentService service;

    @Override
    public Mono<CartPaymentResponse> handle(CartPaymentRequest.Process request) {
        var dto = CartMapper.toProcessRequest(request);
        return this.service.process(dto)
                           .map(processed -> CartMapper.toProcessedResponse(request,processed))
                           .transform(exceptionHandler(request));
    }

    @Override
    public Mono<CartPaymentResponse> handle(CartPaymentRequest.Refund request) {
        return this.service.refund(request.requestItem().getOrderId())
                           .then(Mono.empty());
    }

    private UnaryOperator<Mono<CartPaymentResponse>> exceptionHandler(CartPaymentRequest.Process request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                           .onErrorResume(CartMapper.toPaymentDeclinedResponse(request));
    }
}