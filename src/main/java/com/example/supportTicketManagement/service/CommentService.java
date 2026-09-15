package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.CommentRequestDto;
import com.example.supportTicketManagement.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto createEmployeeComment(CommentRequestDto requestDto, Long ticketId);

    CommentResponseDto createAgentComment(CommentRequestDto requestDto, Long ticketId);
}
