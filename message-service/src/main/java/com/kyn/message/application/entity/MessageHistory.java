package com.kyn.message.application.entity;


import java.util.UUID;

import org.bson.types.ObjectId;
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
    private UUID _id;

    @Field("USER_ID")
    private UUID userId;

    @Field("ORDER_ID")
    private UUID orderId;

    @Field("MESSAGE")
    private String message;

}
