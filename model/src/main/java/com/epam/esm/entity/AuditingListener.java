package com.epam.esm.entity;


import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditingListener {

    // make an auditing before a resource created
    @PrePersist
    void onPrePersist(BaseEntity baseEntity) {
        baseEntity.setCreateDate(LocalDateTime.now());
        baseEntity.setLastUpdateDate(baseEntity.getCreateDate());
    }

    // make an auditing before a resource updated
    @PreUpdate
    void onPreUpdate(BaseEntity baseEntity){
        baseEntity.setLastUpdateDate(LocalDateTime.now());
    }
}
