package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//Dependency Injection
@RequiredArgsConstructor

//@Validated
public class CategoryController {
    private final CategoryService categoryService;

    //Neu tham so truyen vao la 1 object => Data transfer Object = Request Object
    @PostMapping("")
    public ResponseEntity<?> createCategories(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Insert category successfully");
    }
    //Hien thi tat ca categories
    @GetMapping("") //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<List> getAllCategories(
            @RequestParam("page")   int page,
            @RequestParam("limit")  int limit
    ){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategories(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
            ){
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id " + id + " successfully");
    }
}
