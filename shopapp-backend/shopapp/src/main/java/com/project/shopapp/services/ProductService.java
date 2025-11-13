package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.invalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.responses.ProductResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final ProductImageRepository productimageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
      Category existingCategory = categoryRepository
              .findById(productDTO.getCategoryId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "cannot find category with id: "+productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .descripstion(productDTO.getDescription())
                .category(existingCategory)
                .build();

        Product savedProduct = productRepository.save(newProduct); //save vao data base
        //
        if (savedProduct.getId() == null) {
            throw new RuntimeException("Lỗi: ID của sản phẩm bị null sau khi lưu vào database!");
        }

        System.out.println("Sản phẩm được tạo thành công với ID: " + savedProduct.getId());
        return savedProduct;
       // return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws DataNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("Cannot find product with id:"+productId));
    }

    @Override
    public Page<ProductResponses> getAllProducts(PageRequest pageRequest) {
        //Lay danh sach san pham theo trang(page) va gioi han(limit)
        return productRepository
                .findAll(pageRequest)
                .map(ProductResponses::fromProduct); // tham chieu methor
    }

    @Override
    public Product updateProduct(
            long id,
            ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if(existingProduct!= null){
            // coppy cac thuoc tinh tu cai DTO -> Product
            // co the su dungj modelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(()->
                            new DataNotFoundException(
                                    "cannot find category with id: "+productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescripstion(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
            return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(()->
                        new DataNotFoundException(
                                "cannot find product with id: "+productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //ko cho insert qua 5 anh cho 1 san pham
        int size =productimageRepository.findByProductId(productId).size();
        if(size >=ProductImage.MAXIMUN_IMAGES_PER_PRODUCT){
            throw new invalidParamException(
                    "Number of image must be <= "
                    + ProductImage.MAXIMUN_IMAGES_PER_PRODUCT );
        }
        return productimageRepository.save(newProductImage);
    }
}
