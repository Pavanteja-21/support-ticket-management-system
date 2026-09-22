package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.enums.Status;
import com.example.supportTicketManagement.exception.*;
import com.example.supportTicketManagement.repository.TicketRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.TicketService;
import com.example.supportTicketManagement.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    private final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);


    // Employee creates a ticket
    @Override
    @Transactional
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto) {
        log.info("Inside TicketService.createTicket() method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in TicketService.createTicket()");
                    return new UsernameNotFoundException("Username not found");
                });

        Ticket ticket = new Ticket();

        ticket.setTitle(requestDto.getTitle());
        ticket.setDescription(requestDto.getDescription());
        ticket.setPriority(requestDto.getPriority());
        ticket.setEmployee(user);
        ticket.setStatus(Status.OPEN);

        Ticket savedTicket = ticketRepository.save(ticket);

        String ticketNo = String.format("TKT-%04d", savedTicket.getId());

        log.info("Added ticketNo to ticket");

        savedTicket.setTicketNumber(ticketNo);

        log.info("Successfully saved the ticket in db, end of the TicketService.createTicket() method");

        return mapper.mapToCreateTicket(savedTicket);
    }

    // Gets all created tickets
    @Override
    public List<TicketResponseDto> findAllTickets() {
        log.info("Inside TicketService.findAllTickets() method");
        List<Ticket> tickets = ticketRepository.findAll();

        log.info("Successfully returned all tickets from db, end of the TicketService.findAllTickets() method");
        return tickets.stream()
                .map(mapper::mapToTicketResponseDto)
                .toList();
    }

    // Gets all tickets created by specific employee
    @Override
    public List<CreateTicketResponseDto> findAllMyTicktets() {
        log.info("Inside TicketService.findAllMyTicktets() method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in TicketService.findAllMyTickets()");
                    return new UsernameNotFoundException("User is not found");
                });

        List<Ticket> tickets = ticketRepository.findByEmployeeId(user.getId());

        log.info("Successfully return all tickets from db, end of the TicketService.findAllMyTickets() method");

        return  tickets.stream()
                .map(mapper::mapToCreateTicket)
                .toList();
    }

    // Assigning Tickets to Agents
    @Override
    public AssignTicketResponseDto assignTicketToAgent(Long ticketId, Long agentId) {
        log.info("Inside TicketService.assignTicketToAgent() method");

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in TicketService.assignTicketToAgent()");
                    return new AgentNotFoundException("Agent not found");
                });

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    log.info("Threw TicketNotFoundException as ticket is not found in TicketService.assignTicketToAgent()");
                    return new TicketNotFoundException("Ticket not found");
                });

        if(ticket.getStatus().equals(Status.CLOSED)) {
            log.info("Threw TicketClosedException as ticket is already closed in TicketService.assignTicketToAgent()");
            throw new TicketClosedException("Ticket is already closed");
        }

        ticket.setAgent(agent);
        Ticket savedTicket = ticketRepository.save(ticket);

        log.info("Successfully saved the assigned ticket in db, end of the TicketService.assignTicketToAgent() method");

        return mapper.mapToAssignTicket(savedTicket);
    }

    // Gets all tickets assigned to specific agent
    @Override
    public List<AgentTicketResponseDto> findAllMyAgentTickets() {
        log.info("Inside TicketService.findAllMyAgentTickets() method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in TicketService.findAllMyAgentTickets()");
                    return new UsernameNotFoundException("User is not found");
                });

        List<Ticket> tickets = ticketRepository.findByAgentId(user.getId());

        log.info("Successfully returned all tickets from db, end of the TicketService.findAllMyAgentTickets() method");

        return tickets.stream()
                .map(mapper::mapToAgentTicket)
                .toList();
    }

    // Agent can update the ticket status
    @Override
    public TicketStatusResponseDto updateTicketStatus(TicketStatusRequestDto requestDto) {
        log.info("Inside TicketService.updateTicketStatus() method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in TicketService.updateTicketStatus()");
                    return new AgentNotFoundException("User is not found");
                });

        Ticket ticket = ticketRepository.findByIdAndAgentId(requestDto.getTicketId(), user.getId())
                .orElseThrow(() -> {
                    log.info("Threw TicketNotFoundException as ticket is not found in TicketService.updateTicketStatus()");
                    return new TicketNotFoundException("Ticket not found");
                });


        if(ticket.getStatus().equals(Status.OPEN) && requestDto.getStatus().equals(Status.CLOSED)) {
            log.info("Threw TicketCannotClosedException as ticket can't be closed immediately after opening the ticket in TicketService.updateTicketStatus()");
            throw new TicketCannotClosedException("Ticket can't be closed immediately after opening the ticket");
        }

        if(ticket.getStatus().equals(Status.CLOSED)) {
            log.info("Threw TicketClosedException as ticket is already closed in TicketService.updateTicketStatus()");
            throw new TicketClosedException("Ticket is already closed");
        }



        ticket.setStatus(requestDto.getStatus());

        Ticket updatedTicket = ticketRepository.save(ticket);

        log.info("Successfully updated the ticket status in db, end of the TicketService.updateTicketStatus() method");

        return mapper.mapToTicketStatusDto(updatedTicket);
    }

    // Employee can close the ticket, if ticket is resolved
    @Override
    public TicketStatusResponseDto closeTicketStatus(Long ticketId) {
        log.info("Inside TicketService.closeTicketStatus() method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("User is not authenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found in TicketService.closeTicketStatus()");
                    return new UsernameNotFoundException("User is not found");
                });

        Ticket ticket = ticketRepository.findByIdAndEmployeeId(ticketId, user.getId())
                .orElseThrow(() -> {
                    log.info("Threw TicketNotFoundException as ticket is not found in TicketService.closeTicketStatus()");
                    return new TicketNotFoundException("Ticket not found");
                });

        if(!ticket.getStatus().equals(Status.RESOLVED)) {
            log.info("Threw TicketCannotClosedException as ticket can't be closed immediately after opening the ticket in TicketService.closeTicketStatus()");
            throw new TicketCannotClosedException("Ticket cannot closed immediately after opening the ticket");
        }

        ticket.setStatus(Status.CLOSED);

        Ticket updatedTicket = ticketRepository.save(ticket);

        log.info("Successfully closed the ticket in TicketService.closeTicketStatus() method");

        return mapper.mapToTicketStatusDto(updatedTicket);
    }


}
