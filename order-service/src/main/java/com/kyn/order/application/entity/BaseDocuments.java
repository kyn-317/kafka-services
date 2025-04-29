package com.kyn.order.application.entity;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
public class BaseDocuments {
    
    @Column("CREATED_BY")
    private String createdBy;

    @Column("CREATED_AT")
    private LocalDateTime createdAt;

    @Column("UPDATED_BY")
    private String updatedBy;

    @Column("UPDATED_AT")
    private LocalDateTime updatedAt;

    public void insertDocument(String id) {
        this.createdBy = id;
        this.createdAt = LocalDateTime.now();
        this.updatedBy = id;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDocument(String id) {
        this.updatedBy = id;
        this.updatedAt = LocalDateTime.now();
    }

}
