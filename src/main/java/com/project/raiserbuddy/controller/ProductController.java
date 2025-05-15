package com.project.raiserbuddy.controller;

import com.project.raiserbuddy.config.AppConstants;
import com.project.raiserbuddy.dto.*;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Tag(name="User", description="The User API")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/{categoryId}/{providerId}", consumes = {MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8" })
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Integer categoryId, @PathVariable Integer providerId) {

        ProductDTO savedProduct = productService.createProduct(categoryId, providerId, product);

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

    @GetMapping("/filter")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,@RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort,
                                                                      @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){


        Page<Product> res= productService.getProductsInUser(category, minPrice, maxPrice, minDiscount, sort,stock,pageNumber,pageSize);

        System.out.println("complete products");
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);

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

    @PutMapping(value ="/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8" })
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

    @GetMapping("/top10")
    public List<ProdDTO> getTop10BestSellingProducts()
    {
        return productService.getTop10BestSellingProducts();
    }

}
