package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dtos.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id).get();
		return new ProductDTO(product);
		
	}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> products = productRepository.findAll(pageable);
		return products.map(x -> new ProductDTO(x));		
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product p = new Product();
		copyDtoToEntity(dto, p);
		p = productRepository.save(p);
		
		return new ProductDTO(p);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		Product p = productRepository.getReferenceById(id);
		copyDtoToEntity(dto, p);		
		p = productRepository.save(p);
		
		return new ProductDTO(p);
	}

	private void copyDtoToEntity(ProductDTO dto, Product p) {
		p.setName(dto.getName());
		p.setDescription(dto.getDescription());
		p.setImgUrl(dto.getImgUrl());
		p.setPrice(dto.getPrice());
	}
}
