package com.project.raiserbuddy.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.raiserbuddy.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "ourusers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OurUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

//    @Pattern(regexp = "^[a-zA-Z]*$", message = "Name must not contain numbers or special characters")
    private String name;
    private String password;

    @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits long")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
    private String mobile_number;


    @Enumerated(EnumType.STRING)
    private Role role;

    private String image;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("addRef")
    private List<Address> addresses=new ArrayList<>();

    private String block_reason;

    @Embedded
    @ElementCollection
    @CollectionTable(name="payment_information",joinColumns = @JoinColumn(name="user_id"))
    private List<PaymentInformation> paymentInformation=new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("ratingRef")
    private List<Rating> ratings=new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("revRef")
    private List<Reviews> reviews=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("userOrderRef")
    private List<Order> orders=new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("wishRef")
    private Set<Wishlist> wishlists;

    private LocalDateTime createdAt;

    private boolean enabled;

    private String otp;

    private Double walletBalance;

    private LocalDateTime otpGeneratedTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
