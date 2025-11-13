package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity // để biet là thực thể trong javaspring
@Table(name ="categories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Category {

    @Id // de xac dinh khoa chinh
    @GeneratedValue(strategy = GenerationType.IDENTITY) // khóa chính tự động tăng lên 1
    private Long id;

    @Column(name= "name", nullable = false)
    private String name;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
