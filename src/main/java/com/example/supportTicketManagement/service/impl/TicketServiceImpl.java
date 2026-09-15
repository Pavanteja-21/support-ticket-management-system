package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.CreateTicketRequestDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.enums.Status;
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

//        User user = (User) authentication.getPrincipal();

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


}
