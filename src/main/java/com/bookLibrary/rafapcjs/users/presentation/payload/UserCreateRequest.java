package com.bookLibrary.rafapcjs.users.presentation.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserCreateRequest {

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name cannot exceed 100 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name cannot exceed 100 characters.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(max = 255, message = "Email cannot exceed 255 characters.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters.")
    private String phoneNumber;

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;

    @NotBlank(message = "DNI is required.")
    @Size(min = 8, max = 20, message = "DNI must be between 8 and 20 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "DNI must be alphanumeric.")
    private String dni;

}