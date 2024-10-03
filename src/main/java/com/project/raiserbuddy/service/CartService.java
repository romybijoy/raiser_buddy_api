package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.AddItemRequest;
import com.project.raiserbuddy.dto.CartDTO;
import com.project.raiserbuddy.dto.ProductDTO;
import com.project.raiserbuddy.entity.Cart;
import com.project.raiserbuddy.entity.CartItem;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.exceptions.APIException;
import com.project.raiserbuddy.exceptions.ProductException;
import com.project.raiserbuddy.exceptions.ResourceNotFoundException;
import com.project.raiserbuddy.repository.CartItemRepository;
import com.project.raiserbuddy.repository.CartRepository;
import com.project.raiserbuddy.repository.ProductRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartService {

//    @Autowired
//    private CartRepository cartRepo;
//
//    @Autowired
//    private ProductRepository productRepo;
//
//    @Autowired
//    private CartItemRepository cartItemRepo;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//
//    public CartDTO addProductToCart(Integer cartId, Integer productId, Integer quantity) {
//
//        Cart cart = cartRepo.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId, 404));
//
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId, 404));
//
//        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//        if (cartItem != null) {
//            throw new APIException("Product " + product.getName() + " already exists in the cart");
//        }
//
//        if (product.getQuantity() == 0) {
//            throw new APIException(product.getName() + " is not available");
//        }
//
//        if (product.getQuantity() < quantity) {
//            throw new APIException("Please, make an order of the " + product.getName()
//                    + " less than or equal to the quantity " + product.getQuantity() + ".");
//        }
//
//        CartItem newCartItem = new CartItem();
//
//        newCartItem.setProduct(product);
//        newCartItem.setCart(cart);
//        newCartItem.setQuantity(quantity);
//        newCartItem.setDiscount(product.getDiscount());
//        newCartItem.setProductPrice(product.getSpecialPrice());
//
//        cartItemRepo.save(newCartItem);
//
//        product.setQuantity(product.getQuantity() - quantity);
//
//        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//        List<ProductDTO> productDTOs = cart.getCartItems().stream()
//                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//        cartDTO.setProducts(productDTOs);
//
//        return cartDTO;
//
//    }
//
//
//    public List<CartDTO> getAllCarts() {
//        List<Cart> carts = cartRepo.findAll();
//
//        if (carts.size() == 0) {
//            throw new APIException("No cart exists");
//        }
//
//        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
//            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//            List<ProductDTO> products = cart.getCartItems().stream()
//                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//            cartDTO.setProducts(products);
//
//            return cartDTO;
//
//        }).collect(Collectors.toList());
//
//        return cartDTOs;
//    }
//
//
//    public CartDTO getCart(String emailId, Integer cartId) {
//        Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
//
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart", "cartId", cartId, 404);
//        }
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//        List<ProductDTO> products = cart.getCartItems().stream()
//                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//        cartDTO.setProducts(products);
//
//        return cartDTO;
//    }
//
//
//    public void updateProductInCarts(Integer cartId, Integer productId) {
//        Cart cart = cartRepo.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId, 404));
//
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId, 404));
//
//        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new APIException("Product " + product.getName() + " not available in the cart!!!");
//        }
//
//        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//        cartItem.setProductPrice(product.getSpecialPrice());
//
//        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//        cartItem = cartItemRepo.save(cartItem);
//    }
//
//
//    public CartDTO updateProductQuantityInCart(Integer cartId, Integer productId, Integer quantity) {
//        Cart cart = cartRepo.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId, 404));
//
//        Product product = productRepo.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId,404));
//
//        if (product.getQuantity() == 0) {
//            throw new APIException(product.getName() + " is not available");
//        }
//
//        if (product.getQuantity() < quantity) {
//            throw new APIException("Please, make an order of the " + product.getName()
//                    + " less than or equal to the quantity " + product.getQuantity() + ".");
//        }
//
//        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new APIException("Product " + product.getName() + " not available in the cart!!!");
//        }
//
//        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//        product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);
//
//        cartItem.setProductPrice(product.getSpecialPrice());
//        cartItem.setQuantity(quantity);
//        cartItem.setDiscount(product.getDiscount());
//
//        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));
//
//        cartItem = cartItemRepo.save(cartItem);
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//        List<ProductDTO> productDTOs = cart.getCartItems().stream()
//                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//        cartDTO.setProducts(productDTOs);
//
//        return cartDTO;
//
//    }
//
//
//    public String deleteProductFromCart(Integer cartId, Integer productId) {
//        Cart cart = cartRepo.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId,404));
//
//        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new ResourceNotFoundException("Product", "productId", productId,404);
//        }
//
//        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//        Product product = cartItem.getProduct();
//        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
//
//        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);
//
//        return "Product " + cartItem.getProduct().getName() + " removed from the cart !!!";
//    }

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UsersRepository userRepo;


//    public CartServiceImplementation(CartRepository cartRepository,CartItemService cartItemService,
//                                     ProductService productService) {
//        this.cartRepository=cartRepository;
//        this.productService=productService;
//        this.cartItemService=cartItemService;
//    }


    public Cart createCart(OurUsers user) {

        Cart cart = new Cart();
        cart.setUser(user);
        Cart createdCart=cartRepository.save(cart);
        return createdCart;
    }

    public Cart findUserCart(String email) {
        OurUsers givenUser = userRepo.findByEmail(email).orElseThrow();
        Cart cart =	cartRepository.findByUserId(givenUser.getId());
        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;
        for(CartItem cartsItem : cart.getCartItems()) {
            totalPrice+=cartsItem.getPrice();
            totalDiscountedPrice+=cartsItem.getDiscountedPrice();
            totalItem+=cartsItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(cart.getCartItems().size());
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscount(totalPrice-totalDiscountedPrice);
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);

    }


    public CartItem addCartItem(String email, AddItemRequest req) throws ProductException {
        OurUsers givenUser = userRepo.findByEmail(email).orElseThrow();
        Cart cart=cartRepository.findByUserId(givenUser.getId());
        Product product=productService.findProductById(req.getProductId());

        CartItem isPresent=cartItemService.isCartItemExist(cart, product,givenUser.getId());

        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(givenUser.getId());


            double price=req.getQuantity()*product.getSpecialPrice();
            cartItem.setPrice(price);
            CartItem createdCartItem=cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
            return createdCartItem;
        }


        return isPresent;
    }

}
