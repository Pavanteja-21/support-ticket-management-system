package com.example.supportTicketManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private boolean active;

    private Set<RoleResponseDto> roles = new HashSet<>();
}
