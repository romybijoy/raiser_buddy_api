package com.project.raiserbuddy.dto;

import com.project.raiserbuddy.entity.Address;
import com.project.raiserbuddy.entity.OrderItem;
import com.project.raiserbuddy.entity.OurUsers;
import com.project.raiserbuddy.entity.PaymentDetails;
import com.project.raiserbuddy.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer id;

    private String orderId;

    private UserDTO user;

    private List<OrderItemsDTO> orderItems;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    private AddressDTO shippingAddress;

    private PaymentDetails paymentDetails;

    private double totalPrice;

    private Integer totalDiscountedPrice;

    private Integer discount;

    private OrderStatus orderStatus;

    private int totalItem;

    private LocalDateTime createdAt;

    public OrderDTO(Integer id, List<OrderItem> orderItems, OurUsers user, LocalDateTime orderDate, Integer discount, LocalDateTime deliveryDate, OrderStatus orderStatus, Integer totalDiscountedPrice, double totalPrice, int totalItem, AddressDTO addressDTO) {
    }
}
