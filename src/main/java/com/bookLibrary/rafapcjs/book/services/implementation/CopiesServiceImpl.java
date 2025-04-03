package com.bookLibrary.rafapcjs.book.services.implementation;

import com.bookLibrary.rafapcjs.book.persistencie.entities.Book;
import com.bookLibrary.rafapcjs.book.persistencie.entities.Copies;
import com.bookLibrary.rafapcjs.book.persistencie.repositories.IBooksRepository;
import com.bookLibrary.rafapcjs.book.persistencie.repositories.ICopiesRepository;
import com.bookLibrary.rafapcjs.book.presentation.dto.CopyDto;
import com.bookLibrary.rafapcjs.book.presentation.payload.CopyPayload;
import com.bookLibrary.rafapcjs.book.services.interfaces.ICopiesServices;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CopiesServiceImpl implements ICopiesServices {

    final  private ICopiesRepository iCopiesRepository;
    final private ModelMapper modelMapper;
    final private IBooksRepository iBooksRepository;
    @Override
    @Transactional()
    public void save(CopyPayload copyPayload) {

        Copies copies =modelMapper.map(copyPayload, Copies.class);
        Book book = iBooksRepository.findByUuid(copyPayload.getBookUuid()).orElseThrow(()->new ResourceNotFoundException("libro no existe"));
        copies.setBook(book);
        iCopiesRepository.save(copies);
    }

    @Override
    public void update(CopyPayload copyPayload, UUID uuid) {

    }

    @Override
    public void deleteByUuid(UUID uuid) {
Copies copies =iCopiesRepository.findByUuid(uuid).orElseThrow(()->new ResourceNotFoundException("libro no existe"));
iCopiesRepository.delete(copies);
    }

    @Override
    public Page<CopyDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<CopyDto> findByBookUuid(UUID bookUuid, Pageable pageable) {
        return null;
    }

    @Override
    public CopyDto findByUuid(UUID uuid) {
        return null;
    }
}
