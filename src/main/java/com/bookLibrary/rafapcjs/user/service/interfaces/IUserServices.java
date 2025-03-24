package com.bookLibrary.rafapcjs.user.service.interfaces;

import com.bookLibrary.rafapcjs.user.presentation.dto.UserDto;
import com.bookLibrary.rafapcjs.user.presentation.payload.UserPayload;

public interface IUserServices {

    UserDto createUser(UserPayload payload);
}
