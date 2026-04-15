package com.project.raiserbuddy.service;

import com.project.raiserbuddy.dto.ProdDTO;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.Product;
import com.project.raiserbuddy.entity.WishlistItem;
import com.project.raiserbuddy.repository.ProductRepository;
import com.project.raiserbuddy.repository.UsersRepository;
import com.project.raiserbuddy.repository.WishlistItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WishlistService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private UsersRepository userRepo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    // GET
    public List<ProdDTO> getWishlist(Integer userId) {
        return wishlistItemRepository.findByUserId(userId)
                .stream()
                .map(item -> modelMapper.map(item.getProduct(), ProdDTO.class))
                .toList();
    }

    // TOGGLE (ADD / REMOVE)
    public void toggleWishlist(Integer userId, Integer productId) {

        Optional<WishlistItem> existing =
                wishlistItemRepository.findByUserIdAndProductProductId(userId, productId);

        if (existing.isPresent()) {
            wishlistItemRepository.delete(existing.get());
        } else {
            OurUsers user = userRepo.findById(userId).orElseThrow();
            Product product = productRepository.findById(productId).orElseThrow();

            WishlistItem item = new WishlistItem();
            item.setUser(user);
            item.setProduct(product);
            item.setCreatedAt(LocalDateTime.now());

            wishlistItemRepository.save(item);
        }
    }

    // REMOVE (optional)
    public void removeFromWishlist(Integer userId, Integer productId) {
        wishlistItemRepository.deleteByUserIdAndProductProductId(userId, productId);
    }

}