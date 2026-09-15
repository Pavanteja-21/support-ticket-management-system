package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.AgentTicketResponseDto;
import com.example.supportTicketManagement.dto.TicketStatusRequestDto;
import com.example.supportTicketManagement.dto.TicketStatusResponseDto;
import com.example.supportTicketManagement.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@PreAuthorize("hasRole('SUPPORT_AGENT')")
@RequiredArgsConstructor
public class AgentController {

    private final TicketService ticketService;

    @GetMapping("/tickets")
    public ResponseEntity<List<AgentTicketResponseDto>> findAllMyAgentTickets(){
        return ResponseEntity
                .ok(ticketService.findAllMyAgentTickets());
    }

    @PatchMapping("/ticket/update/status")
    public ResponseEntity<TicketStatusResponseDto> updateTicketStatus(@RequestBody @Valid
                                                                           TicketStatusRequestDto requestDto){

        return  ResponseEntity
                .ok(ticketService.updateTicketStatus(requestDto));

    }
}
