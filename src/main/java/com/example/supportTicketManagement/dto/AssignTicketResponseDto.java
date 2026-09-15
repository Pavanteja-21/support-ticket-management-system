package com.example.supportTicketManagement.dto;

import com.example.supportTicketManagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignTicketResponseDto {
    private Long ticketId;

    private String ticketNumber;

    private UserResponseDto agent;

    private Status status;
}
