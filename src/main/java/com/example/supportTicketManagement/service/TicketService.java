package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.CreateTicketRequestDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;

import java.util.List;

public interface TicketService {

    CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto);

    List<CreateTicketResponseDto> findAllTickets();

    List<CreateTicketResponseDto> findAllMyTicktets();
}
