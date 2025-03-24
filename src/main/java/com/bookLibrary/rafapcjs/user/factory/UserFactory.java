package com.bookLibrary.rafapcjs.user.factory;

import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import com.bookLibrary.rafapcjs.user.presentation.dto.UserDto;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final ModelMapper modelMapper;

    //convertir un UserPayload a una entidad User


    // Convertir una entidad User a un UserDto
    public UserDto userDto(UserEntity userEntity) {

        return UserDto.builder()
                .fullName(userEntity.getUsername())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .createDate(userEntity.getCreateDate())
                .build();
    }

}
