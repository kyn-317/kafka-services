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

    @Field("REGR_ID")
    @CreatedBy
    private String regrId;

    @Field("REG_DT")
    @CreatedDate
    private LocalDateTime regDt;

    @Field("AMDR_ID")
    @LastModifiedBy
    private String amdrId;

    @Field("AMD_DT")
    @LastModifiedDate
    private LocalDateTime amdDt;

    public void insertDocument(String id) {
        this.regrId = id;
        this.regDt = LocalDateTime.now();
        this.amdrId = id;
        this.amdDt = LocalDateTime.now();
    }

    public void updateDocument(String id) {
        this.amdrId = id;
        this.amdDt = LocalDateTime.now();
    }
}
