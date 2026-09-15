package com.example.supportTicketManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    private String lastName;

    @Email(message = "Email must be in valid format")
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Length(min = 4, max = 10, message = "Password must be between 4 and 10 characters of size")
    private String password;

    @NotBlank(message = "Role is required")
    private String roleName;

}
