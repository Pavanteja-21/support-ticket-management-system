package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.*;
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
@RequestMapping("/api/agent")
@PreAuthorize("hasRole('SUPPORT_AGENT')")
@RequiredArgsConstructor
public class AgentController {

    private final TicketService ticketService;
    private final CommentService commentService;

    // Agent can view all his assigned tickets
    @GetMapping("/tickets")
    public ResponseEntity<List<AgentTicketResponseDto>> findAllMyAgentTickets(){
        return ResponseEntity
                .ok(ticketService.findAllMyAgentTickets());
    }

    // Agent can update the status of his assigned tickets
    @PatchMapping("/ticket/update/status")
    public ResponseEntity<TicketStatusResponseDto> updateTicketStatus(@RequestBody @Valid
                                                                           TicketStatusRequestDto requestDto){

        return  ResponseEntity
                .ok(ticketService.updateTicketStatus(requestDto));

    }

    // Agent can add ticket
    @PostMapping("/{ticketId}/add/comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto requestDto,
                                                            @PathVariable Long ticketId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createAgentComment(requestDto, ticketId));
    }
}
