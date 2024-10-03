package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.CategoryDTO;
import com.project.raiserbuddy.dto.CategoryResponse;
import com.project.raiserbuddy.dto.ProductResponse;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Tag(name="Category", description="The Category API")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody Category category){
        CategoryDTO response = categoryService.createCategory(category);

        return new ResponseEntity<CategoryDTO>(response, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                                        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
                                                             @RequestParam(name = "status", defaultValue = AppConstants.STATUS, required = false) boolean status){

        CategoryResponse categoryResponse = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder,status);

        return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.FOUND);
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<CategoryResponse> getCategoriesByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        CategoryResponse productResponse = categoryService.searchCategoryByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<CategoryResponse>(productResponse, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getUSerByID(@PathVariable Integer id){
        CategoryDTO response = categoryService.getCategoryById(id);
        System.out.println(response);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<CategoryDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer id, @RequestBody Category reqres){
        CategoryDTO response = categoryService.updateCategory(id, reqres);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<CategoryDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
