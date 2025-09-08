package com.devsuperior.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dtos.OrderDTO;
import com.devsuperior.dscommerce.dtos.OrderItemDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.enums.OrderStatus;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new OrderDTO(order);
		
	}
	
//	@Transactional(readOnly = true)
//	public Page<ProductDTO> findAll(Pageable pageable) {
//		Page<Product> products = productRepository.findAll(pageable);
//		return products.map(x -> new ProductDTO(x));		
//	}
//	
//	@Transactional(readOnly = true)
//	public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
//		Page<Product> products = productRepository.searchByName(name, pageable);
//		return products.map(x -> new ProductMinDTO(x));		
//	}
//	
	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		Order order = new Order();
		
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		
		User user = userService.authenticated();
		order.setClient(user);
		
		for (OrderItemDTO itemDTO : dto.getItems()) {
			Product p = productRepository.getReferenceById(itemDTO.getProductId());
			OrderItem item = new OrderItem(order, p, itemDTO.getQuantity(), p.getPrice());
			order.getItems().add(item);
		}
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());
		return new OrderDTO(order);
	}
//	
//	@Transactional
//	public ProductDTO update(Long id, ProductDTO dto) {
//		try {
//			Product p = productRepository.getReferenceById(id);
//			copyDtoToEntity(dto, p);		
//			p = productRepository.save(p);
//			return new ProductDTO(p);
//		} catch(EntityNotFoundException r) {
//			throw new ResourceNotFoundException("Recurso não encontrado");
//		}
//	}
//	
//	@Transactional(propagation = Propagation.SUPPORTS)
//	public void delete(Long id) {
//		if (!productRepository.existsById(id)) {
//			throw new ResourceNotFoundException("Recurso não encontrado");
//		}
//		try {
//	       	productRepository.deleteById(id);    		
//		}catch (DataIntegrityViolationException e) {
//	       	throw new DatabaseException("Falha de integridade referencial");
//	   	}
//
//	}
//
//	private void copyDtoToEntity(ProductDTO dto, Product p) {
//		p.setName(dto.getName());
//		p.setDescription(dto.getDescription());
//		p.setImgUrl(dto.getImgUrl());
//		p.setPrice(dto.getPrice());
//		
//		p.getCategories().clear();
//		for(CategoryDTO catDTO : dto.getCategories()) {
//			//Category cat = new Category();
//			//cat.setId(catDTO.getId());
//			
//			Category cat = categoryRepository.getReferenceById(catDTO.getId());
//			p.getCategories().add(cat);
//		}
//	}
}
