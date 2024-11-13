package com.project.raiserbuddy.service;

import com.project.raiserbuddy.entity.OrderItem;
import com.project.raiserbuddy.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
@Autowired
	private OrderItemRepository orderItemRepository;

	public OrderItem createOrderItem(OrderItem orderItem) {
		
		return orderItemRepository.save(orderItem);
	}

}
