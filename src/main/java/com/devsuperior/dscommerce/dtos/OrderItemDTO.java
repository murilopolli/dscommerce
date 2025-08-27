package com.devsuperior.dscommerce.dtos;

import java.math.BigDecimal;

import com.devsuperior.dscommerce.entities.OrderItem;

public class OrderItemDTO {

	private Long productId;
	private String name;
	private BigDecimal price;
	private Integer quantity;
	
	public OrderItemDTO(Long productId, String name, BigDecimal price, Integer quantity) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	public OrderItemDTO(OrderItem entity) {
		productId = entity.getProduct().getId();
		name = entity.getProduct().getName();
		price = entity.getPrice();
		quantity = entity.getQuantity();
	}

	public Long getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}
	
	public BigDecimal getSubTotal() {
		return price.multiply(BigDecimal.valueOf(quantity));
	}
	
}
