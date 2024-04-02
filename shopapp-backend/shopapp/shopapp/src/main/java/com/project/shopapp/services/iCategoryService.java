package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface iCategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO category);
    void deleteCategory(long id);
}
