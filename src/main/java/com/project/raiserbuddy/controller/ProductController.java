package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.*;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Tag(name="User", description="The User API")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Integer categoryId) {

        ProductDTO savedProduct = productService.createProduct(categoryId, product);

        return new ResponseEntity<ProductDTO>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                     @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                     @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                     @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
                                                     @RequestParam(name = "status", defaultValue = AppConstants.STATUS, required = false) boolean status){

       ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, status);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductByID(@PathVariable Integer id){
        ProductDTO response = productService.getProductById(id);
        System.out.println(response);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ProductDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);

    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

//   @PutMapping("/{id}")
//    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody Product reqres){
//        ProductDTO response = productService.updateProduct(id, reqres);
//        if(response.getStatusCode() == 500){
//            return new ResponseEntity<ProductDTO>(response,HttpStatus.BAD_REQUEST);
//        }
//        return ResponseEntity.ok(response);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product,
                                                    @PathVariable Integer id) {
        ProductDTO updatedProduct = productService.updateProduct(id, product);

        return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Integer id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductDTO> getProductByCategoryId(@PathVariable Integer categoryId){
        ProductDTO response = productService.getProductByCategory(categoryId);
        System.out.println(response);
        if(response.getStatusCode() == 500){
            return new ResponseEntity<ProductDTO>(response,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);

    }

}