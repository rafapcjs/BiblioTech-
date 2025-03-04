package com.bookLibrary.rafapcjs.commons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public  abstract  class BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(nullable = false , updatable = false, name = "create_date")
    private Date createDate;



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;


    @PrePersist
    public void generateUuid() {
        uuid = UUID.randomUUID();
    }

    @PreUpdate
    public  void preUpdate (){
        updateDate =new Date();
    }

}
