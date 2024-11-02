package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name= "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer add_id;

    private String house_name;
    private String place;
    private String district;
    private String state;
    private String country;
    private String zipcode;
    private String mobile;
    private boolean is_default;

//    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY)
//    @JsonIgnoreProperties("addresses")
//    private List<OurUsers> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user",  referencedColumnName = "id")
    @JsonBackReference("addRef")
    private OurUsers user;

    @OneToMany(mappedBy = "shippingAddress", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference("ordAddRef")
    private Set<Order> orders = new HashSet<>();
}
