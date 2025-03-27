package com.bookLibrary.rafapcjs.security.auth.service;

import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthCreateUserRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.payload.AuthLoginRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthResponse;
import com.bookLibrary.rafapcjs.security.auth.service.login.AuthLoginService;
import com.bookLibrary.rafapcjs.security.auth.service.sign_in.AuthRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRegisterService authRegisterService;
    private final AuthLoginService authLoginService;

    public AuthResponse register(AuthCreateUserRequest request) {
        return authRegisterService.register(request);
    }

    public AuthResponse login(AuthLoginRequest request) {
        return authLoginService.login(request);
    }

}
