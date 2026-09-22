package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.*;

public interface AuthService {
    UserResponseDto register(UserRegisterDto registerRequestDto);

    void resetPassword(ResetPasswordRequestDto requestDto);
}
