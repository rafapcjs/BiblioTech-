package com.bookLibrary.rafapcjs.security.auth.service.login;

import com.bookLibrary.rafapcjs.security.auth.factory.AuthUserMapper;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomsDetailServices implements UserDetailsService {


    private final UserRepository userRepository;
    private final AuthUserMapper authUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return authUserMapper.toUserDetails(userEntity);
    }
}
