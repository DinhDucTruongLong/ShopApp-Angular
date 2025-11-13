package com.project.shopapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "create_at" )
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist
    protected  void  onCreate(){// tu dong cap nhat thoi gian tren mysql
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }
}
