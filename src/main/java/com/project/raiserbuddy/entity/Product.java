package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
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

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade =  {CascadeType.PERSIST,CascadeType.MERGE })
//    @JsonIgnoreProperties                             ("product")

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    private List<Reviews> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", referencedColumnName = "category_id")
    @JsonIgnoreProperties("product")
    private Category category;

    private boolean status;

    private double discount;
    private double specialPrice;

}
