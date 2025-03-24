package com.bookLibrary.rafapcjs.user.service.implementation;

import com.bookLibrary.rafapcjs.user.factory.UserFactory;
import com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository;
import com.bookLibrary.rafapcjs.user.service.interfaces.IUserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServicesImpl implements IUserServices {

    private final UserRepository userRepository;
    private final UserFactory userFactory;




}
