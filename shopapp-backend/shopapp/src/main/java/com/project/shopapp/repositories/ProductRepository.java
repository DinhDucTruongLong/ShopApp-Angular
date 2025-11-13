package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // san pham voi ten do co ton tai hay khono
    boolean existsByName(String name);

    @Override
    Page<Product> findAll(Pageable pageable);// phan trang cai san pham

}
