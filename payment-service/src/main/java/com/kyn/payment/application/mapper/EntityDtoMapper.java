package com.kyn.payment.application.mapper;

import java.util.UUID;

import com.kyn.payment.application.entity.CustomerPayment;
import com.kyn.payment.common.dto.CartPaymentDto;
import com.kyn.payment.common.dto.CartPaymentProcessRequest;

public class EntityDtoMapper {

    public static CustomerPayment toCustomerPayment(CartPaymentProcessRequest request, UUID accountId) {
        return CustomerPayment.builder()
                              .accountId(accountId)
                              .orderId(request.orderId())
                              .amount(request.amount())
                              .build();
    }

    public static CartPaymentDto toDto(CustomerPayment payment) {
        return CartPaymentDto.builder()
                         .amount(payment.getAmount())
                         .status(payment.getStatus())
                         .paymentId(payment.getPaymentId())
                         .accountId(payment.getAccountId())
                         .orderId(payment.getOrderId())
                         .build();
    }

}