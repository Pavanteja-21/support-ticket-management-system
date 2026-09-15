package com.example.supportTicketManagement.dto;

import com.example.supportTicketManagement.enums.Priority;
import com.example.supportTicketManagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {
    private Long id;

    private String ticketNumber;

    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserResponseDto employee;

    private UserResponseDto agent;
}
