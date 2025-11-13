package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_images")
public class ProductImage {
    public static final int MAXIMUN_IMAGES_PER_PRODUCT = 5 ; // cho 5 anh tren 1 san pham

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 300)
    private String imageUrl;

}
