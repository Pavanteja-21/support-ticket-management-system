package com.example.supportTicketManagement.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestDto {
    @NotEmpty(message = "Current password is required")
    private String currentPassword;

    @NotEmpty(message = "enter new password")
    private String newPassword;

    @NotEmpty(message = "confirm password is required")
    private String confirmNewPassword;
}
