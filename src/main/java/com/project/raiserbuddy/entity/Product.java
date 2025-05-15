package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope=Product.class,generator=ObjectIdGenerators.PropertyGenerator.class, property="productId")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prod_id")
    private Integer productId;

    private String name;

    @Column(length = 1200)
    private List<String> images = new ArrayList<>();
    private String shortDesc;
    private String desc;
    private Double avgRating;
    private Double price;
    private Double quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("reviewRef")
    private List<Reviews> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("ratRef")
    private List<Rating> ratings = new ArrayList<>();

//    @Column(name = "num_ratings")
//    private int numRatings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", referencedColumnName = "category_id")
    @JsonBackReference("procatref")
    private Category category;

//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference("prodWishRef")
//    private Set<Wishlist> wishlists;

    @ManyToMany(mappedBy = "products")
//    @JsonBackReference("wishProRef")
    private Set<Wishlist> wishlists = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider", referencedColumnName = "id")
    @JsonBackReference("providerProRef")
    private Provider provider;

    private boolean status;
    private int sales;
    private double discount;
    private double specialPrice;
    private LocalDateTime createdAt;
}
