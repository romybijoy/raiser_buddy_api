package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name= "category")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Integer categoryId;

    @NotBlank
    private String name;


    @Column(name="status")
    private Boolean status=true;

    @NotBlank
    private String desc;

    @NotBlank
    private String image;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade =  {CascadeType.ALL })
    @JsonIgnoreProperties("category")
    private List<Product> products;

}
