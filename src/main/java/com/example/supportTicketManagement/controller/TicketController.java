package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.CreateTicketRequestDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;
import com.example.supportTicketManagement.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // Only Employee can create the ticket
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<CreateTicketResponseDto> createTicket(@RequestBody @Valid
                                                                CreateTicketRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketService.createTicket(requestDto));

    }
}
