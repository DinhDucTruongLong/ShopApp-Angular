package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    //@Query("SELECT pi FROM ProductImage pi WHERE pi.productId.id = :productId")
    List<ProductImage> findByProductId(Long productId);
}
