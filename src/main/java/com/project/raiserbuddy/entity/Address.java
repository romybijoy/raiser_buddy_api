package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private boolean is_default;

    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("addresses")
    private List<OurUsers> users = new ArrayList<>();
}
