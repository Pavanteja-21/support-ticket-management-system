package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.CommentRequestDto;
import com.example.supportTicketManagement.dto.CommentResponseDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;
import com.example.supportTicketManagement.dto.TicketStatusResponseDto;
import com.example.supportTicketManagement.service.CommentService;
import com.example.supportTicketManagement.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // Gets all tickets created by employee
    @GetMapping("/mytickets")
    public ResponseEntity<List<CreateTicketResponseDto>> getAllMyTickets() {
        return ResponseEntity
                .ok(ticketService.findAllMyTicktets());
    }

    // Employee can add comment to his ticket
    @PostMapping("/{ticketId}/add/comment")
    public ResponseEntity<CommentResponseDto> addComment(@RequestBody @Valid
                                                             CommentRequestDto requestDto,
                                                         @PathVariable Long ticketId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createEmployeeComment(requestDto, ticketId));
    }

    // Employee can close the resolved tickets
    @PatchMapping("/{ticketId}/close")
    public ResponseEntity<TicketStatusResponseDto> closeTicket(@PathVariable Long ticketId) {

        return ResponseEntity
                .ok(ticketService.closeTicketStatus(ticketId));
    }
}
