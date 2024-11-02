package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.*;
import com.project.raiserbuddy.entity.Category;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.Reviews;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.exceptions.ResourceNotFoundException;
import com.project.raiserbuddy.repository.CategoryRepository;
import com.project.raiserbuddy.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

//    public ProductDTO createProduct(ProductDTO request){
//        ProductDTO resp = new ProductDTO();
//
//        try {
//            Product product = new Product();
//            product.setName(request.getName());
//            product.setDesc(request.getDesc());
//            product.setShortDesc(request.getShortDesc());
//            product.setImages(request.getImages());
//            product.setAvgRating(request.getAvgRating());
//            product.setPrice(request.getPrice());
//            product.setCategory(request.getCategory());
//            product.setReviews(request.getReviews());
//            product.setStatus(true);
//            Product result = productRepository.save(product);
//            if (result.getProductId()>0) {
//                resp.setProduct((result));
//                resp.setMessage("Product Saved Successfully");
//                resp.setStatusCode(200);
//            }
//
//        }catch (Exception e){
//            resp.setStatusCode(500);
//            resp.setError(e.getMessage());
//        }
//        return resp;
//    }

    public Product findProductById(Integer id) throws ProductException {
        Optional<Product> opt=productRepository.findById(id);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new ProductException("product not found with id "+id);
    }

    public ProductDTO createProduct(Integer categoryId, Product product) {

        ProductDTO resp = new ProductDTO();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId, 404));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product.getName())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
//            product.setImage("default.png");

            product.setCategory(category);
product.setDiscount(product.getDiscount());
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
product.setStatus(true);
            Product savedProduct = productRepository.save(product);
            resp.setMessage("Product Saved Successfully");
            resp.setStatusCode(201);
            modelMapper.map(savedProduct, resp);

            return resp;
        } else {
            throw new APIException("Product already exists !!!", 409);
        }
    }


//    public ProductDTO getProducts(String keyword) {
//        ProductDTO productDTO = new ProductDTO();
//        List<Product> result;
//        try {
//            if(!keyword.isEmpty()){
//                result = productRepository.search(keyword);
//            }else{
//                result = productRepository.findAllByStatus();
//            }
//            if (!result.isEmpty()) {
//                productDTO.setProductList(result);
//                productDTO.setStatusCode(200);
//                productDTO.setMessage("Successful");
//            } else {
//                productDTO.setStatusCode(404);
//                productDTO.setMessage("No products found");
//            }
//            return productDTO;
//        } catch (Exception e) {
//            productDTO.setStatusCode(500);
//            productDTO.setMessage("Error occurred: " + e.getMessage());
//            return productDTO;
//        }
//    }

    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Boolean status) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageCategories;
        pageCategories = productRepository.findByStatus(status, pageDetails);

        List<Product> products = pageCategories.getContent();

        List<ProdDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProdDTO.class))
                .toList();

        if (products.isEmpty()) {
            throw new APIException("No product is created till now", 404);
        }

//        List<Product> productDTOs = categories.stream().collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setMessage("Product fetched Successfully");
        productResponse.setStatusCode(302);
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageCategories.getNumber());
        productResponse.setPageSize(pageCategories.getSize());
        productResponse.setTotalElements(pageCategories.getTotalElements());
        productResponse.setTotalPages(pageCategories.getTotalPages());
        productResponse.setLastPage(pageCategories.isLast());

        return productResponse;
    }


    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepository.findByNameIgnoreCaseContainingAndStatus(keyword, true, pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.size() == 0) {
            throw new APIException("Products not found with keyword: " + keyword, 404);
        }

        List<ProdDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProdDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();

        productResponse.setMessage("Products fetched Successfully");
        productResponse.setStatusCode(302);
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    public ProductDTO getProductById(Integer id) {
        ProductDTO productDTO = new ProductDTO();

        try {
            Product productById = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not found"));
//           List<ReviewDTO> reviews = productById.getReviews().stream()
//                    .map(review -> new ReviewDTO(review.getReview_id(), review.getReview(),review.getRating(),review.getUser().getName(),review.getCreatedAt()))
//                    .collect(Collectors.toList());

            ProdDTO product = modelMapper.map(productById, ProdDTO.class);
            productDTO.setProduct(product);
//            productDTO.setReviews(reviews);
            productDTO.setStatusCode(200);
            productDTO.setMessage("Product with id '" + id + "' found successfully");
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return productDTO;
    }

    public Page<Product> getProductsInUser(String category, Integer minPrice, Integer maxPrice,
                                       Integer minDiscount,String sort, String stock, Integer pageNumber, Integer pageSize ) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);



        if(stock!=null) {

            if(stock.equals("in_stock")) {
                products=products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
            }
            else if (stock.equals("out_of_stock")) {
                products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());
            }


        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);
        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
        return filteredProducts; // If color list is empty, do nothing and return all products


    }

    public ProductDTO getProductByCategory(Integer id) {
        ProductDTO productDTO = new ProductDTO();
        try {
            List<Product> products;
            if(id==0){
                products  = productRepository.findByStatus(true);
            }
           products = productRepository.findProductByCategoryId(id);

            if (products == null) {
                throw new ResourceNotFoundException("Product", "categoryId", id, 404);
            }

            List<ProdDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProdDTO.class))
                    .toList();
            productDTO.setProductList(productDTOs);
            productDTO.setStatusCode(200);
            productDTO.setMessage("Product with category id '" + id + "' found successfully");
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Error occurred: " + e.getMessage());
        }
        return productDTO;
    }



    public ProductDTO deleteProduct(Integer id) {
        ProductDTO productDTO = new ProductDTO();
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                productRepository.updateStatus(id);
                productDTO.setStatusCode(200);
                productDTO.setMessage("Product deleted successfully");
            } else {
                productDTO.setStatusCode(404);
                productDTO.setMessage("Product not found for deletion");
            }
        } catch (Exception e) {
            productDTO.setStatusCode(500);
            productDTO.setMessage("Error occurred while deleting Product: " + e.getMessage());
        }
        return productDTO;
    }

//    public ProductDTO updateProduct(Integer id, Product request) {
//        ProductDTO productDTO = new ProductDTO();
//        try {
//            Optional<Product> productOptional = productRepository.findById(id);
//            if (productOptional.isPresent()) {
//                Product existingProduct = productOptional.get();
//                existingProduct.setName(request.getName());
//                existingProduct.setDesc(request.getDesc());
//                existingProduct.setShortDesc(request.getShortDesc());
//                existingProduct.setImages(request.getImages());
//                existingProduct.setAvgRating(request.getAvgRating());
//                existingProduct.setCategory(request.getCategory());
//                existingProduct.setReviews(request.getReviews());
//                existingProduct.setStatus(request.isStatus());
//
//                Product savedProduct = productRepository.save(existingProduct);
//                productDTO.setProduct(savedProduct);
//                productDTO.setStatusCode(200);
//                productDTO.setMessage("Product updated successfully");
//            } else {
//                productDTO.setStatusCode(404);
//                productDTO.setMessage("Product not found for update");
//            }
//        } catch (Exception e) {
//            productDTO.setStatusCode(500);
//            productDTO.setMessage("Error occurred while updating Product: " + e.getMessage());
//        }
//        return productDTO;
//    }


    public ProductDTO updateProduct(Integer productId, Product product) {
        ProductDTO resp = new ProductDTO();
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId, 404));

        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }

        product.setProductId(productId);
        product.setName(productFromDB.getName());
        product.setCategory(productFromDB.getCategory());
        product.setReviews(productFromDB.getReviews());
        product.setStatus(true);
        product.setImages(productFromDB.getImages());
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);

       /* List<Cart> carts = cartRepo.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));*/
        resp.setMessage("Product updated Successfully");
        resp.setStatusCode(201);
        modelMapper.map(savedProduct, resp);
        return resp;
    }



}
