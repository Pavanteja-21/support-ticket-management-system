package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.AssignTicketResponseDto;
import com.example.supportTicketManagement.dto.CreateTicketRequestDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;
import com.example.supportTicketManagement.dto.TicketResponseDto;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Only Admin can view all the created tickets
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity
                .ok(ticketService.findAllTickets());
    }

    // Only Admin can assign the ticket to agent
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{ticketId}/assign/{agentId}")
    public ResponseEntity<AssignTicketResponseDto> assignTicketToAgent(@PathVariable Long ticketId,
                                                                       @PathVariable Long agentId) {

        return ResponseEntity
                .ok(ticketService.assignTicketToAgent(ticketId, agentId));

    }
}
