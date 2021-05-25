package com.devsuperior.dscatalog.services;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {

		List<Category> list = repository.findAll();

		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public CategoryDTO findById(Long id) {

		Optional<Category> obj = repository.findById(id);

		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));

		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {

		Category entity = new Category();

		entity.setName(dto.getName());
		entity = repository.save(entity);

		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {

		try {

			Category entity = repository.getOne(id);

			entity.setName(dto.getName());
			entity = repository.save(entity);

			return new CategoryDTO(entity);

		} catch (EntityNotFoundException e) {
		
			throw new ResourceNotFoundException("Id: " + id + ", not found!");
		}
	}
}