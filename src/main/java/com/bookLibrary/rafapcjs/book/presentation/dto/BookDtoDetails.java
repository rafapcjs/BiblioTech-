package com.bookLibrary.rafapcjs.book.presentation.dto;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;

 public record BookDtoDetails(
        String bookUuid,
        String isbn,
        int quantityPage,
        String title,
        String nameCategoria,
        String descriptionCategoria,
        String uuidCategoria,
        Long cantidadEjemplares,
        String authorUuids,
        String authorFullnames
        , StatusEntity statusEntity
// Nueva propiedad para los nombres completos de los autores
) {
}
