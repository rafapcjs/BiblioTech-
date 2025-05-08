package com.bookLibrary.rafapcjs.book.presentation.dto;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoDetails {

 private String bookUuid;
 private String isbn;
 private int quantityPage;
 private String title;
 private String nameCategoria;
 private String descriptionCategoria;
 private String uuidCategoria;
 private long cantidadEjemplares;
 private String authorUuids;
 private String authorFullnames;
 private String statusEntity;

}