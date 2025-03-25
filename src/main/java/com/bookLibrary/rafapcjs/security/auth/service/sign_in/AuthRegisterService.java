package com.bookLibrary.rafapcjs.security.auth.service.sign_in;

import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthCreateUserRequest;
import com.bookLibrary.rafapcjs.security.auth.controller.dto.AuthResponse;
import com.bookLibrary.rafapcjs.security.auth.factory.AuthUserMapper;
import com.bookLibrary.rafapcjs.security.auth.persistence.model.RoleEntity;
import com.bookLibrary.rafapcjs.security.auth.persistence.repositories.RoleRepository;
import com.bookLibrary.rafapcjs.security.utils.jwt.JwtTokenProvider;
import com.bookLibrary.rafapcjs.user.persistence.entities.UserEntity;
import com.bookLibrary.rafapcjs.user.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthRegisterService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserMapper authUserMapper;

    public AuthResponse register(AuthCreateUserRequest request) {
        Set<RoleEntity> roleEntities = roleRepository
                .findRoleEntitiesByRoleEnumIn(request.roleRequest().roleListName())
                .stream().collect(Collectors.toSet());

        if (roleEntities.isEmpty()) throw new IllegalArgumentException("Roles not found");

        UserEntity user = authUserMapper.toUserEntity(request, roleEntities, passwordEncoder);

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authUserMapper.mapRoles(user.getRoles()));
        String token = jwtTokenProvider.createToken(authentication);

        return new AuthResponse(user.getUsername(), "User created successfully", token, true);
    }
}
