package com.bookLibrary.rafapcjs.user.service.implementation;

import com.bookLibrary.rafapcjs.commons.exception.exceptions.ConflictException;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ServiceUnavailableException;
import com.bookLibrary.rafapcjs.user.factory.UserFactory;
import com.bookLibrary.rafapcjs.user.persistence.entities.User;
import com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository;
import com.bookLibrary.rafapcjs.user.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.user.presentation.payload.UserPayload;
import com.bookLibrary.rafapcjs.user.service.interfaces.IUserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements IUserServices {

    private final UserRepository userRepository;
    private final UserFactory userFactory;

    @Override
    @Transactional
    public UserDto createUser(UserPayload payload) {
        try {

            User user = userFactory.user(payload);
            User savedUser = userRepository.save(user);
            return userFactory.userDto(savedUser);

        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("El usuario ya existe o hay datos duplicados");
        } catch (Exception e) {
            throw new ServiceUnavailableException("Error al crear el usuario");
        }
    }



}
