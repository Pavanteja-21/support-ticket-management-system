package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.CreateTicketRequestDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;

public interface TicketService {

    CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto);
}
