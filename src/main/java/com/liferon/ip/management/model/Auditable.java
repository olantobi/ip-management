package com.liferon.ip.management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY HH:mm:ss.SSSZ")
    @CreatedDate
    @Column(name = "created_date")
    protected LocalDateTime createdDate;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY HH:mm:ss.SSSZ")
//    @LastModifiedDate
//    @Column(name = "last_modified_date")
//    protected LocalDateTime lastModifiedDate;
}
