package com.bookLibrary.rafapcjs.users.serivices.implementation;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ConflictException;
import com.bookLibrary.rafapcjs.commons.exception.exceptions.ResourceNotFoundException;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import com.bookLibrary.rafapcjs.users.persistencie.repositories.UserRepository;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.users.presentation.payload.UserCreateRequest;
import com.bookLibrary.rafapcjs.users.presentation.payload.UserUpdateRequest;
import com.bookLibrary.rafapcjs.users.serivices.interfaces.IUserServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements IUserServices {

    final private UserRepository userRepository;
    final private ModelMapper modelMapper;

    @Override
    @Transactional
    public void save(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByDni(userCreateRequest.getDni())) {
            throw new ConflictException("El dni ya esta en uso");
        }

        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new ConflictException("El email ya esta en uso");
        }

        User user = modelMapper.map(userCreateRequest, User.class);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(UserUpdateRequest userUpdateRequest, String dni) {
        // Buscar el usuario por DNI
        User userExist = userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar el usuario porque no existe"));

        // Verificar si el email existe, pero ignorar si el email no ha cambiado
        if (!userExist.getEmail().equals(userUpdateRequest.getEmail()) && userRepository.existsByEmail(userUpdateRequest.getEmail())) {
            throw new ConflictException("El email ya esta en uso");
        }

        // Mapear los datos del UserUpdateRequest a userExist
        modelMapper.map(userUpdateRequest, userExist);

        // Guardar el usuario actualizado
        userRepository.save(userExist);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(x -> modelMapper.map(x, UserDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede encontrar el usuario"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByDni(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede encontrar el usuario"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public void unNeableUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede encontrar el usuario"));

        user.setStatusEntity(StatusEntity.ARCHIVED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void enableUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede encontrar el usuario"));

        user.setStatusEntity(StatusEntity.ACTIVE);
        userRepository.save(user);
    }
}