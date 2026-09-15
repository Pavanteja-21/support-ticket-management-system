package com.example.supportTicketManagement.dto;

import com.example.supportTicketManagement.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;

    private String comment;

    private LocalDateTime createdAt;

    private TicketStatusResponseDto ticket;

    private UserResponseDto user;
}
