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
@RequestMapping("/api/agent")
@PreAuthorize("hasRole('SUPPORT_AGENT')")
@RequiredArgsConstructor
public class AgentController {

    private final TicketService ticketService;
    private final CommentService commentService;

    private final Logger log = LoggerFactory.getLogger(AgentController.class);

    // Agent can view all his assigned tickets
    @Operation(summary = "View Assigned Tickets", description = "Agent can view all his assigned tickets by Admin")
    @GetMapping("/tickets")
    public ResponseEntity<List<AgentTicketResponseDto>> findAllMyAgentTickets(){
        log.info("Request entered '/api/agent/tickets', findAllMyAgentTickets() method is called");
        return ResponseEntity
                .ok(ticketService.findAllMyAgentTickets());
    }

    // Agent can update the status of his assigned tickets
    @Operation(summary = "Update Status", description = "Agent can update the status of tickets that are assigned to him")
    @PatchMapping("/ticket/update/status")
    public ResponseEntity<TicketStatusResponseDto> updateTicketStatus(@RequestBody @Valid
                                                                           TicketStatusRequestDto requestDto){
        log.info("Request entered '/api/agent/ticket/update/status', updateTicketStatus() method is called");
        return  ResponseEntity
                .ok(ticketService.updateTicketStatus(requestDto));

    }

    // Agent can add comment in ticket
    @Operation(summary = "Add Comment", description = "Agent can add comment in the ticket that are assigned to him")
    @PostMapping("/{ticketId}/add/comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto requestDto,
                                                            @PathVariable Long ticketId) {
        log.info("Request entered '/api/agent/{}/add/comment', createComment() method is called", ticketId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createAgentComment(requestDto, ticketId));
    }
}
