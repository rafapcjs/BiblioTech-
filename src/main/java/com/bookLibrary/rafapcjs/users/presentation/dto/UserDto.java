package com.bookLibrary.rafapcjs.users.presentation.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String dni;
}
