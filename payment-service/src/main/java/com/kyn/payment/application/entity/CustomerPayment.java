package com.kyn.payment.application.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.kyn.common.messages.payment.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(schema = "payment_data", name = "customer_payment")
public class CustomerPayment extends BaseDocuments {

    @Id
    private UUID paymentId;
    private UUID orderId;
    private UUID customerId;
    private PaymentStatus status;
    private Double amount;

}