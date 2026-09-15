package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.entity.Ticket;

import java.util.List;

public interface TicketService {

    CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto);

    List<Ticket> findAllTickets();

    List<CreateTicketResponseDto> findAllMyTicktets();

    AssignTicketResponseDto assignTicketToAgent(Long ticketId, Long agentId);

    List<AgentTicketResponseDto> findAllMyAgentTickets();

}
