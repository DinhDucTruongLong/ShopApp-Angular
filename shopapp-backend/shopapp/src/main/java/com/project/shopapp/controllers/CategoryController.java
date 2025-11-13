package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/categories")
//@Validated
//Dependency Injection
//@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public ResponseEntity<?> createCategories(@Valid @RequestBody CategoryDTO categoryDTO,
                                              BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors() // Lấy danh sách lỗi
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList()); // Chuyển thành List<String>

            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category sucessfully");
    }

    //hien thi tat cả các categori
    @GetMapping("")//http://localhost:8080/api/v1/categories?pare=1&limit=10
    public ResponseEntity<List<Category>> getAllcategories(
            @RequestParam("pare") int pare,
            @RequestParam("limit") int limit
    ){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatecategories(
            @PathVariable Long id,
           @Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update categogies successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletecategories(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("delete insertCategory with id ="+id+"Successfully");
    }
}
