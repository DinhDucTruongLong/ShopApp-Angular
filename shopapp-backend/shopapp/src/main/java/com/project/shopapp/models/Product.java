package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // để biet là thực thể trong javaspring
@Table(name ="products")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name = "thumbnail" , length = 300)
    private String thumbnail;

    @Column(name = "descripstion" )
    private String descripstion;



    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
