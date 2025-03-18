package com.bookLibrary.rafapcjs.categories.service.implementation;

import com.bookLibrary.rafapcjs.categories.factory.CategoryFactory;
import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.persistencie.repositories.CategoryRepository;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.presentation.payload.CategoryPayload;
import com.bookLibrary.rafapcjs.categories.service.interfaces.ICategoryServices;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServicesImpl implements ICategoryServices {


    final  private CategoryRepository  categoryRepository;
    final  private ModelMapper modelMapper ;
    final  private CategoryFactory categoryFactory;

    @Override
    @Transactional()
    public void save(CategoryPayload categoryPayload) {

        Category category = modelMapper.map(categoryPayload , Category.class) ;
        categoryRepository.save(category);

    }

    @Override
    @Transactional
    public void update(CategoryPayload categoryPayload, UUID uuid) {

        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con UUID: " + uuid));

        category.setName(categoryPayload.getName());
        category.setDescription(categoryPayload.getDescription());
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findByDescription(String description) {

        return  this.categoryRepository.findByDescription(description)
                .map(category -> categoryFactory.createCategoryDto(category)


                ).orElseThrow(()->new ResourceNotFoundException(" categoria no encontrada : " + description));



    }
        @Override
        @Transactional(readOnly = true)

        public CategoryDto findByUuid(UUID uuid) {
            return  this.categoryRepository.findByUuid(uuid)
                    .map(category -> categoryFactory.createCategoryDto(category))
                    .orElseThrow(()->new ResourceNotFoundException(" categoria no encontrada : " + uuid));

        }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findByName(String name) {
        return categoryRepository.findByName(name)
                .map(categoryFactory::createCategoryDto)  // ← Si `createCategoryDto` retorna un `Category`
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con nombre: " + name));
    }


    @Override
    @Transactional()

    public void deleteByUuid(UUID uuid) {
        Category findByUuid = categoryRepository.findByUuid(uuid).orElseThrow( () -> new ResourceNotFoundException("categoria no encontrada con UUID: " + uuid));
        categoryRepository.delete(findByUuid);

    }
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryFactory::createCategoryDto);
    }

}
