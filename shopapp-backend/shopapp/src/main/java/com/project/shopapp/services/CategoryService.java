package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor // kiem tra thuoc tinh nao
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
//        // Tạo Category object bằng constructor trực tiếp
//        CategoryDTO newCategory = new CategoryDTO(); // Sử dụng constructor mới chỉ có name
//        return categoryRepository.save(newCategoryDTO);
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catergory not found") );
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId,
                                    CategoryDTO categoryDTO) {
        Category exsitingCategory = getCategoryById(categoryId);
        exsitingCategory.setName(categoryDTO.getName());
        categoryRepository.save(exsitingCategory);
        return exsitingCategory;
    }

    @Override
    public void deleteCategory(long id) {
        // xoa cung
        categoryRepository.deleteById(id);
    }
}
