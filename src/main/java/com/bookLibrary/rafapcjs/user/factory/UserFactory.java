package com.bookLibrary.rafapcjs.user.factory;

import com.bookLibrary.rafapcjs.user.persistence.entities.User;
import com.bookLibrary.rafapcjs.user.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.user.presentation.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final ModelMapper modelMapper;

    //convertir un UserPayload a una entidad User
    public User user(UserPayload userPayload) {
        return modelMapper.map(userPayload, User.class);
    }

    // Convertir una entidad User a un UserDto
    public UserDto userDto(User user) {

        return UserDto.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createDate(user.getCreateDate())
                .build();
    }

}
