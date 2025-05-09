package com.bookLibrary.rafapcjs.fines.presentation.dto;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class FineDto {
    private UUID   id;
    private UUID loanId;
    private String userDni;
    private BigDecimal amount;
    private StatusEntity status;
    private LocalDate issuedDate;
}