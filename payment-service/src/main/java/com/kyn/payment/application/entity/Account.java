package com.kyn.payment.application.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
@Table(schema = "payment_data", name = "account")
public class Account extends BaseDocuments {

    @Id
    private UUID id;
    private UUID customerId;
    private Double balance;

}