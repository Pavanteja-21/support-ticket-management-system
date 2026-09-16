package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.service.CommentService;
import com.example.supportTicketManagement.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
@RequiredArgsConstructor
public class EmployeeController {

    private final TicketService ticketService;
    private final CommentService commentService;

    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    // Only Employee can create the ticket
    @Operation(summary = "Create Ticket", description = "Only Employee can create their ticket")
    @PostMapping("/add/ticket")
    public ResponseEntity<CreateTicketResponseDto> createTicket(@RequestBody @Valid
                                                                CreateTicketRequestDto requestDto) {
        log.info("Request entered '/api/employee/add/ticket', createTicket() method is called");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketService.createTicket(requestDto));

    }

    // Gets all tickets created by employee
    @Operation(summary = "View Created Tickets", description = "Only Employee can view their created tickets")
    @GetMapping("/tickets")
    public ResponseEntity<List<CreateTicketResponseDto>> getAllMyTickets() {
         log.info("Request entered '/api/employee/tickets', getAllMyTickets() method is called");
        return ResponseEntity
                .ok(ticketService.findAllMyTicktets());
    }

    // Employee can add comment to his ticket
    @Operation(summary = "Add Comment", description = "Employee can add comment to his ticket")
    @PostMapping("/{ticketId}/add/comment")
    public ResponseEntity<CommentResponseDto> addComment(@RequestBody @Valid
                                                             CommentRequestDto requestDto,
                                                         @PathVariable Long ticketId) {
        log.info("Request entered '/api/employee/{}/add/comment', addComment() method is called", ticketId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createEmployeeComment(requestDto, ticketId));
    }

    // Employee can close their resolved tickets
    @Operation(summary = "Close Ticket", description = "Employee can close their resolved tickets")
    @PatchMapping("/{ticketId}/close")
    public ResponseEntity<TicketStatusResponseDto> closeTicket(@PathVariable Long ticketId) {
        log.info("Request entered '/api/employee/{}/close', closeTicket() method is called", ticketId);
        return ResponseEntity
                .ok(ticketService.closeTicketStatus(ticketId));
    }
}
