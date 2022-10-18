package com.epam.esm.entity;


import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditingListener {

    @PrePersist
    void onPrePersist(BaseEntity baseEntity) {
        baseEntity.setCreateDate(LocalDateTime.now());
        baseEntity.setLastUpdateDate(baseEntity.getCreateDate());
    }

    @PreUpdate
    void onPreUpdate(BaseEntity baseEntity){
        baseEntity.setLastUpdateDate(LocalDateTime.now());
    }
}
