package com.kyn.payment.application.mapper;

import com.kyn.payment.application.entity.CustomerPayment;
import com.kyn.payment.common.dto.CartPaymentDto;
import com.kyn.payment.common.dto.CartPaymentProcessRequest;

public class EntityDtoMapper {

    public static CustomerPayment toCustomerPayment(CartPaymentProcessRequest request) {
        return CustomerPayment.builder()
                              .customerId(request.customerId())
                              .orderId(request.orderId())
                              .amount(request.amount())
                              .build();
    }

    public static CartPaymentDto toDto(CustomerPayment payment) {
        return CartPaymentDto.builder()
                         .amount(payment.getAmount())
                         .status(payment.getStatus())
                         .paymentId(payment.getPaymentId())
                         .customerId(payment.getCustomerId())
                         .orderId(payment.getOrderId())
                         .build();
    }

}