package com.devsuperior.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dtos.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Recurso não encontrado"));
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
		try {
			Product p = productRepository.getReferenceById(id);
			copyDtoToEntity(dto, p);		
			p = productRepository.save(p);
			return new ProductDTO(p);
		} catch(EntityNotFoundException r) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
	       	productRepository.deleteById(id);    		
		}catch (DataIntegrityViolationException e) {
	       	throw new DatabaseException("Falha de integridade referencial");
	   	}

	}

	private void copyDtoToEntity(ProductDTO dto, Product p) {
		p.setName(dto.getName());
		p.setDescription(dto.getDescription());
		p.setImgUrl(dto.getImgUrl());
		p.setPrice(dto.getPrice());
	}
}
