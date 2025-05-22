package com.kyn.message.application.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDocuments {

    @Field("CREATED_BY")
    @CreatedBy
    private String createdBy;

    @Field("CREATED_DT")
    @CreatedDate
    private LocalDateTime createdDt;

    @Field("UPDATED_BY")
    @LastModifiedBy
    private String updatedBy;

    @Field("UPDATED_DT")
    @LastModifiedDate
    private LocalDateTime updatedDt;

    public void insertDocument(String id) {
        this.createdBy = id;
        this.createdDt = LocalDateTime.now();
        this.updatedBy = id;
        this.updatedDt = LocalDateTime.now();
    }

    public void updateDocument(String id) {
        this.updatedBy = id;
        this.updatedDt = LocalDateTime.now();
    }
}
