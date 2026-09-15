package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.enums.Status;
import com.example.supportTicketManagement.exception.TicketClosedException;
import com.example.supportTicketManagement.exception.TicketNotFoundException;
import com.example.supportTicketManagement.repository.TicketRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.TicketService;
import com.example.supportTicketManagement.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;


    // Employee creates a ticket
    @Override
    @Transactional
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Ticket ticket = new Ticket();

        ticket.setTitle(requestDto.getTitle());
        ticket.setDescription(requestDto.getDescription());
        ticket.setPriority(requestDto.getPriority());
        ticket.setEmployee(user);
        ticket.setStatus(Status.OPEN);

        Ticket savedTicket = ticketRepository.save(ticket);

        String ticketNo = String.format("TKT-%04d", savedTicket.getId());

        savedTicket.setTicketNumber(ticketNo);

        return mapper.mapToCreateTicket(savedTicket);
    }

    // Gets all created tickets
    @Override
    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    // Gets all tickets created by specific employee
    @Override
    public List<CreateTicketResponseDto> findAllMyTicktets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<Ticket> tickets = ticketRepository.findByEmployeeId(user.getId());

        return  tickets.stream()
                .map(mapper::mapToCreateTicket)
                .toList();
    }

    // Assigning Tickets to Agents
    @Override
    public AssignTicketResponseDto assignTicketToAgent(Long ticketId, Long agentId) {

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new UsernameNotFoundException("Agent not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if(ticket.getStatus().equals(Status.CLOSED)) {
            throw new TicketClosedException("Ticket is already closed");
        }

        ticket.setAgent(agent);
        Ticket savedTicket = ticketRepository.save(ticket);

        return mapper.mapToAssignTicket(savedTicket);
    }

    // Gets all tickets assigned to specific agent
    @Override
    public List<AgentTicketResponseDto> findAllMyAgentTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<Ticket> tickets = ticketRepository.findByAgentId(user.getId());

        return tickets.stream()
                .map(mapper::mapToAgentTicket)
                .toList();
    }

    // Agent can update the ticket status
    @Override
    public TicketStatusResponseDto updateTicketStatus(TicketStatusRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Ticket ticket = ticketRepository.findByIdAndAgentId(requestDto.getTicketId(), user.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));


        if(ticket.getStatus().equals(Status.OPEN) && requestDto.getStatus().equals(Status.CLOSED)) {
            throw new TicketClosedException("Ticket can't be closed immediately after opening the ticket");
        }

        if(ticket.getStatus().equals(Status.CLOSED)) {
            throw new TicketClosedException("Ticket is already closed");
        }



        ticket.setStatus(requestDto.getStatus());

        Ticket updatedTicket = ticketRepository.save(ticket);

        return mapper.mapToTicketStatusDto(updatedTicket);
    }


}
