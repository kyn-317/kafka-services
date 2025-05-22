package com.kyn.message.application.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document("MESSAGE_HISTORY")
public class MessageHistory extends BaseDocuments {
    @Id
    private String _id;

    @Field("USER_ID")
    private String userId;

    @Field("ORDER_ID")
    private String orderId;

    @Field("MESSAGE")
    private String message;

}
