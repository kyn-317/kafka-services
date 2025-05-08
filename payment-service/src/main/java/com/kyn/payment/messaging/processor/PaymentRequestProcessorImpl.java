/* package com.kyn.payment.messaging.processor;

import java.util.function.UnaryOperator;

import org.springframework.stereotype.Service;

import com.kyn.common.exception.EventAlreadyProcessedException;
import com.kyn.common.messages.payment.PaymentRequest;
import com.kyn.common.messages.payment.PaymentResponse;
import com.kyn.payment.common.service.PaymentService;
import com.kyn.payment.messaging.mapper.MessageDtoMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentRequestProcessorImpl implements PaymentRequestProcessor {

    private final PaymentService service;

    @Override
    public Mono<PaymentResponse> handle(PaymentRequest.Process request) {
        var dto = MessageDtoMapper.toProcessRequest(request);
        return this.service.process(dto)
                           .map(MessageDtoMapper::toProcessedResponse)
                           .transform(exceptionHandler(request));
    }

    @Override
    public Mono<PaymentResponse> handle(PaymentRequest.Refund request) {
        return this.service.refund(request.orderId())
                           .then(Mono.empty());
    }

    private UnaryOperator<Mono<PaymentResponse>> exceptionHandler(PaymentRequest.Process request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                           .onErrorResume(MessageDtoMapper.toPaymentDeclinedResponse(request));
    }

} */