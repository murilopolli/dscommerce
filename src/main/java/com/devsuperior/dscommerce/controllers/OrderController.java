package com.devsuperior.dscommerce.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscommerce.dtos.OrderDTO;
import com.devsuperior.dscommerce.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
	
	@Autowired
	private OrderService service;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<OrderDTO> fincById(@PathVariable Long id) {
		OrderDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	/*
	 * @GetMapping() public ResponseEntity<Page<ProductMinDTO>> fincALl(
	 * 
	 * @RequestParam(defaultValue = "") String name, Pageable pageable) {
	 * Page<ProductMinDTO> page = productService.findAll(name, pageable); return
	 * ResponseEntity.ok(page); }
	 * 
	 * @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	 */
	 @PostMapping 
	 public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto) { 
		 dto = service.insert(dto); 
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequest() .path("/{id}")
				 .buildAndExpand(dto.getId()).toUri(); 
		 return	 ResponseEntity.created(uri).body(dto); }
	  
	 /* @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	 * 
	 * @PutMapping(value = "/{id}") public ResponseEntity<ProductDTO>
	 * update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) { dto =
	 * productService.update(id, dto); return ResponseEntity.ok(dto); }
	 * 
	 * @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	 * 
	 * @DeleteMapping(value = "/{id}") public ResponseEntity<Void>
	 * delete(@PathVariable Long id) { productService.delete(id); return
	 * ResponseEntity.noContent().build(); }
	 */
}
