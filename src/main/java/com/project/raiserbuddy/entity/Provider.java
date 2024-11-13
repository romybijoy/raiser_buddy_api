package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.raiserbuddy.enums.ProviderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "provider")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Provider{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private ProviderType type;

    @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits long")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
    private String mobile_number;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",  referencedColumnName = "add_id", nullable = false)
    private Address address;


    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("providerProRef")
    private Set<Product> products;

    private LocalDateTime createdAt;

    private boolean status;



}
