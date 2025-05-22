package com.kyn.payment.application.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
public class BaseDocuments {
    
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public void insertDocument(String userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDocument(String userId) {
        this.updatedBy = userId;
        
        this.updatedAt = LocalDateTime.now();
    }
    
}
