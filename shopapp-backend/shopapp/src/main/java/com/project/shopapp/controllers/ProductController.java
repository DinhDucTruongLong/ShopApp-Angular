package com.project.shopapp.controllers;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;

import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListReponse;
import com.project.shopapp.responses.ProductResponses;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;



@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController {

    private final IProductService productService;

    //value ko có đường dẫn cụ theer api chỉ châp nhận dang multipart chứa fila hình
    // ảnh thông qua mẫu form.
    //@RequestBody chỉ supost json chứ không hỗ trợ file
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO, // sử dimg modelattribute để nhận file
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors() // Lấy danh sách lỗi
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList()); // Chuyển thành List<String>
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUN_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only update maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            // duyet 1 vong for cho mang
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // kiểm tra kích thước hình ảnh
                if (file.getSize() > 10 * 1024 * 1024)// kích thước >10mb kiem tra tưng file
                {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is to large");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File must be unimage");
                }
                // lưa file và cập nhật thumbnail vào DTO
                    String filename = storeFile(file); // thay thê hàm code của ban để lưu file
                    // lưu file vào đối tượng product trong Db =>sẽ làm sau
                    ProductImage productImage = productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .imageUrl(filename)
                                    .build()
                    );
                    productImages.add(productImage);
                 }
                return ResponseEntity.ok().body(productImages);// tra về danh sach các ảnh được inport
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private String storeFile(MultipartFile file) throws IOException{
        if(!isImageFile(file)){
            throw new IOException(" Invalid image format");
        }
        if(file.getOriginalFilename() == null){
            throw new IOException(" Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm UUID vào trước tên file để file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString()+"_"+ filename;
        //Dường dãn mà bạn muốn lưu file trong project của bạn
       java.nio.file.Path uploatDir = Paths.get("Uploads");
       //kiem tra thu muc upload này đã tồn tại hay chua.
        if(!Files.exists(uploatDir)){
            Files.createDirectories(uploatDir);
        }
        // đường dẫn đầy đủ tới file
        java.nio.file.Path destination = Paths.get(uploatDir.toString(), uniqueFilename);
        // sao chep file vao thu muc dích.
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);//neu có thì sẽ bị thay thế
        return uniqueFilename; // băt buojc file phải duy nhât
    }
    private boolean isImageFile (MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    @GetMapping("")
    public ResponseEntity<ProductListReponse> getProduct(
        @RequestParam("pare") int pare,
        @RequestParam("limit") int limit
    ){
        // tạo pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                pare, limit,
                Sort.by("CreateAt").descending());// sap xep theo thoi gian moi cũ.
        Page<ProductResponses> productsPage = productService.getAllProducts(pageRequest);
        // lấy Tong số trang
        int totalPages = productsPage.getTotalPages();
        List<ProductResponses> products = productsPage.getContent();
        return ResponseEntity.ok(ProductListReponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(
            @PathVariable("id") Long productId)
    {
        try {
           Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponses.fromProduct(existingProduct)); // tái sử dụng hàm
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product delete with %d successfult", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

 //   @PostMapping("/generateFakeProducts") này chỉ làm nhiệm vụ fake data thui
    public ResponseEntity<?> generateFakeProducts(){ // khi nào muốn dùng thì bỏ //
        Faker faker = new Faker();
        for(int i= 0; i < 5_000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(2,5 ))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Faker products create sucessfully");

    }
}
