package com.bookLibrary.rafapcjs.users.serivices.interfaces;

import com.bookLibrary.rafapcjs.commons.enums.StatusEntity;
import com.bookLibrary.rafapcjs.users.persistencie.entities.User;
import com.bookLibrary.rafapcjs.users.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.users.presentation.payload.UserCreateRequest;
import com.bookLibrary.rafapcjs.users.presentation.payload.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserServices {
    void save (UserCreateRequest userCreateRequest);
    void update (UserUpdateRequest userUpdateRequest ,String dni);
       Page<UserDto> findByStatusEntity(StatusEntity statusEntity, Pageable pageable) ;

    UserDto findByEmail(String email);
    UserDto findByDni(String dni);
  void unNeableUser(String email);
    void enableUser(String email);

}
