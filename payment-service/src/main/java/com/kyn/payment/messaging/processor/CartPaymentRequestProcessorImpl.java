package com.kyn.payment.messaging.processor;

import org.springframework.stereotype.Service;

import com.kyn.common.messages.payment.CartPaymentRequest;
import com.kyn.common.messages.payment.CartPaymentResponse;
import com.kyn.common.processor.CartRequestProcessor;
import com.kyn.common.processor.RequestProcessor;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CartPaymentRequestProcessorImpl implements CartPaymentRequestProcessor {
    private final CartPaymentService service;

    @Override
    public Mono<CartPaymentResponse> handle(CartPaymentRequest.Process request) {
        var dto = MessageDtoMapper.toProcessRequest(request);
        return this.service.process(dto)
                           .map(MessageDtoMapper::toProcessedResponse)
                           .transform(exceptionHandler(request));
    }

    @Override
    public Mono<CartPaymentResponse> handle(CartPaymentRequest.Refund request) {
        return this.service.refund(request.orderId())
                           .then(Mono.empty());
    }

    private UnaryOperator<Mono<CartPaymentResponse>> exceptionHandler(CartPaymentRequest.Process request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                           .onErrorResume(MessageDtoMapper.toPaymentDeclinedResponse(request));
    }
}