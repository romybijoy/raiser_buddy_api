package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.CategoryDTO;
import com.project.raiserbuddy.dto.CategoryResponse;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ResourceNotFoundException;
import com.project.raiserbuddy.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    public CategoryDTO  createCategory(Category category){
        CategoryDTO resp = new CategoryDTO();
        Category savedCategory = categoryRepository.findByName(category.getName());

        if (savedCategory != null) {
            throw new APIException("Category with the name '" + category.getName() + "' already exists !!!", 409);
        }
        savedCategory = categoryRepository.save(category);

        System.out.println(savedCategory);
        resp.setMessage("Category Saved Successfully");
        resp.setStatusCode(201);
        modelMapper.map(savedCategory, resp);

        return resp;
    }

//    public CategoryDTO getCategories(String keyword) {
//        CategoryDTO categoryDTO = new CategoryDTO();
//        List<Category> result;
//        try {
//            if(!keyword.isEmpty()){
//                result = categoryRepository.search(keyword);
//            }else{
//                result = categoryRepository.findAllByStatus();
//            }
//            if (!result.isEmpty()) {
//                categoryDTO.setCategories(result);
//                categoryDTO.setStatusCode(200);
//                categoryDTO.setMessage("Successful");
//            } else {
//                categoryDTO.setStatusCode(404);
//                categoryDTO.setMessage("No categories found");
//            }
//            return categoryDTO;
//        } catch (Exception e) {
//            categoryDTO.setStatusCode(500);
//            categoryDTO.setMessage("Error occurred: " + e.getMessage());
//            return categoryDTO;
//        }
//    }

    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Boolean status) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> pageCategories;
        pageCategories = categoryRepository.findByStatus(status, pageDetails);

        List<Category> categories = pageCategories.getContent();

        if (categories.isEmpty()) {
            throw new APIException("No category is created till now", 404);
        }

//        List<Category> categoryDTOs = categories.stream().collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setMessage("Category fetched Successfully");
        categoryResponse.setStatusCode(302);
        categoryResponse.setContent(categories);
        categoryResponse.setPageNumber(pageCategories.getNumber());
        categoryResponse.setPageSize(pageCategories.getSize());
        categoryResponse.setTotalElements(pageCategories.getTotalElements());
        categoryResponse.setTotalPages(pageCategories.getTotalPages());
        categoryResponse.setLastPage(pageCategories.isLast());

        return categoryResponse;
    }

    public CategoryResponse searchCategoryByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> pageCategories = categoryRepository.findByNameIgnoreCaseContainingAndStatus(keyword, true, pageDetails);

        List<Category> categories = pageCategories.getContent();

        if (categories.size() == 0) {
            throw new APIException("Category not found with keyword: " + keyword, 404);
        }

//        List<CategoryDTO> productDTOs = categories.stream().map(p -> modelMapper.map(p, CategoryDTO.class))
//                .collect(Collectors.toList());

        CategoryResponse productResponse = new CategoryResponse();

        productResponse.setMessage("Categories fetched Successfully");
        productResponse.setStatusCode(302);
        productResponse.setContent(categories);
        productResponse.setPageNumber(pageCategories.getNumber());
        productResponse.setPageSize(pageCategories.getSize());
        productResponse.setTotalElements(pageCategories.getTotalElements());
        productResponse.setTotalPages(pageCategories.getTotalPages());
        productResponse.setLastPage(pageCategories.isLast());

        return productResponse;
    }

    public CategoryDTO getCategoryById(Integer category_id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        try {
            Category categoryById = categoryRepository.findById(category_id).orElseThrow(() -> new RuntimeException("Category Not found"));
            categoryDTO.setCategory(categoryById);
            categoryDTO.setStatusCode(200);
            categoryDTO.setMessage("Category with id '" + category_id + "' found successfully");
        } catch (Exception e) {
            categoryDTO.setStatusCode(500);
            categoryDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return categoryDTO;
    }


    public CategoryDTO deleteCategory(Integer id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id, 404));

                categoryRepository.updateStatus(id);
                categoryDTO.setStatusCode(200);
                categoryDTO.setMessage("Category deleted successfully");

        return categoryDTO;
    }

//    public CategoryDTO updateCategory(Integer id, Category updatedCategory) {
//        CategoryDTO categoryDTO = new CategoryDTO();
//        try {
//            Optional<Category> categoryOptional = categoryRepository.findById(id);
//            if (categoryOptional.isPresent()) {
//                Category existingCategory = categoryOptional.get();
//                existingCategory.setName(updatedCategory.getName());
//                existingCategory.setDesc(updatedCategory.getDesc());
//                existingCategory.setImage(updatedCategory.getImage());
//                Category savedCategory = categoryRepository.save(existingCategory);
//                categoryDTO.setCategory(savedCategory);
//                categoryDTO.setStatusCode(200);
//                categoryDTO.setMessage("Category updated successfully");
//            } else {
//                categoryDTO.setStatusCode(404);
//                categoryDTO.setMessage("Category not found for update");
//            }
//        } catch (Exception e) {
//            categoryDTO.setStatusCode(500);
//            categoryDTO.setMessage("Error occurred while updating Category: " + e.getMessage());
//        }
//        return categoryDTO;
//    }

    public CategoryDTO updateCategory(Integer categoryId, Category updatedCategory) {

        CategoryDTO resp = new CategoryDTO();

        Category saveddbCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId, 404));
        Category categoryByName = categoryRepository.findByName(updatedCategory.getName());

        updatedCategory.setCategoryId(categoryId);

        if(categoryByName != null){
            updatedCategory.setName(saveddbCategory.getName());
        }

        Category savedCategory = categoryRepository.save(updatedCategory);

        resp.setMessage("Category updated Successfully");
        resp.setStatusCode(201);
        modelMapper.map(savedCategory, resp);
        return resp;
    }
}
