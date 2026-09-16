package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.AssignTicketResponseDto;
import com.example.supportTicketManagement.dto.TicketResponseDto;
import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.service.AdminService;
import com.example.supportTicketManagement.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final TicketService ticketService;

    // Get the Response for all the users of employee role
    @GetMapping("/employees")
    public ResponseEntity<List<UserResponseDto>> findAllEmployees(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(adminService.getAllEmployees(page, size));
    }

    // Get the Response for all the users of support agent role
    @GetMapping("/agents")
    public ResponseEntity<List<UserResponseDto>> findAllAgents(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(adminService.getAllAgents(page, size));
    }

    // Only Admin can view all the created tickets
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDto>> getAllTickets() {
        return ResponseEntity
                .ok(ticketService.findAllTickets());
    }

    // Only Admin can assign the ticket to agent
    @PatchMapping("/ticket/{ticketId}/assign/{agentId}")
    public ResponseEntity<AssignTicketResponseDto> assignTicketToAgent(@PathVariable Long ticketId,
                                                                       @PathVariable Long agentId) {

        return ResponseEntity
                .ok(ticketService.assignTicketToAgent(ticketId, agentId));

    }
}
