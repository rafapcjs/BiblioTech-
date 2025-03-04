package com.bookLibrary.rafapcjs.categories.service.implementation;

import com.bookLibrary.rafapcjs.categories.factory.CategoryFactory;
import com.bookLibrary.rafapcjs.categories.persistencie.entities.Category;
import com.bookLibrary.rafapcjs.categories.persistencie.repositories.CategoryRepository;
import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.presentation.payload.CategoryPayload;
import com.bookLibrary.rafapcjs.categories.service.interfaces.ICategoryServices;
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

        Optional<Category> findByUuid = categoryRepository.findByUuid(uuid);

        if (findByUuid.isPresent()){
            Category category = findByUuid.orElseThrow();

            category.setName(categoryPayload.getName());
            category.setDescription(categoryPayload.getDescription());
            categoryRepository.save(category);

        }

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryDto> findByDescription(String description) {

        return  this.categoryRepository.findByDescription(description)
                .map(category -> categoryFactory.createCategoryDto(category));



    }
        @Override
        @Transactional(readOnly = true)

        public Optional<CategoryDto> findByUuid(UUID uuid) {
            return  this.categoryRepository.findByUuid(uuid)
                    .map(category -> categoryFactory.createCategoryDto(category));

        }

    @Override
    @Transactional(readOnly = true)

    public Optional<CategoryDto> findByName(String name) {
        return  this.categoryRepository.findByDescription(name)
                .map(category -> categoryFactory.createCategoryDto(category));

    }

    @Override
    @Transactional()

    public void deleteByUuid(UUID uuid) {
        Optional<Category> findByUuid = categoryRepository.findByUuid(uuid);
if (findByUuid.isPresent()){
    categoryRepository.deleteByUuid(uuid);
}

    }

    @Override
    @Transactional(readOnly = true)

    public Page<CategoryDto> findAll(Pageable pageable) {

        Page<Category>categoryPage= categoryRepository.findAll(pageable);

        List<CategoryDto> categoryDtoList = categoryPage.stream() .map(category ->    categoryFactory.createCategoryDto(category))
                .collect(Collectors.toList());

        return  new PageImpl<>(categoryDtoList , pageable ,categoryPage.getTotalElements());
     }
}
