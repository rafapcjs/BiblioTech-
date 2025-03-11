package com.bookLibrary.rafapcjs.categories.presentation.controller;

import com.bookLibrary.rafapcjs.categories.presentation.dto.CategoryDto;
import com.bookLibrary.rafapcjs.categories.presentation.payload.CategoryPayload;
import com.bookLibrary.rafapcjs.categories.service.interfaces.ICategoryServices;
import com.bookLibrary.rafapcjs.commons.utils.pageable.PageableUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

      private  final ICategoryServices iCategoryServices ;

    @PostMapping()
    public ResponseEntity<?> create( @Valid  @RequestBody  CategoryPayload  categoryPayload) throws URISyntaxException {

        iCategoryServices.save(categoryPayload);

        return ResponseEntity.created(new URI("/api/v1/category/")).build();


    }

    @GetMapping()
    public ResponseEntity<?> getAll(

            @RequestParam (defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam (defaultValue = "name"  )String sortBy,
            @RequestParam (defaultValue = "asc"  )String direction)

    {
        Pageable pageable = PageableUtil.createPageable(page, size, sortBy, direction);

        return ResponseEntity.ok(iCategoryServices.findAll(pageable));



    }

    @PutMapping("/update/{uuid}")

    public ResponseEntity<?> update( @Valid @RequestBody CategoryPayload  categoryPayload ,   @PathVariable UUID uuid) {
 iCategoryServices.update(categoryPayload, uuid);
 return ResponseEntity.noContent().build();

    }

        @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {



        iCategoryServices.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/name/{name}")

    public ResponseEntity<?> findName(@PathVariable String name) {


        CategoryDto categoryDto = iCategoryServices.findByName(name);
        return ResponseEntity.ok(categoryDto);

    }

    @GetMapping(value = "/description/{description}")

    public ResponseEntity<?> findDescription(@PathVariable String description) {


        CategoryDto categoryDto = iCategoryServices.findByDescription(description);
        return ResponseEntity.ok(categoryDto);

    }


    @GetMapping(value = "/uuid/{uuid}")

    public ResponseEntity<?> findUuid(@PathVariable UUID uuid) {


        CategoryDto categoryDto = iCategoryServices.findByUuid(uuid);
        return ResponseEntity.ok(categoryDto);

    }

    }
