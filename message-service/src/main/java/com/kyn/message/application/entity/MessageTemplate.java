package com.kyn.message.application.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("MESSAGE_TEMPLATE")
public class MessageTemplate extends BaseDocuments {
    
    @Id
    private String _id;

    @Field("TEMPLATE_NUMBER")
    private Integer templateNumber;

    @Field("TEMPLATE_NAME")
    private String templateName;

    @Field("TEMPLATE_CONTENT")
    private String templateContent;

    @Field("TEMPLATE_TYPE")
    private String templateType;
}
