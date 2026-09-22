package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.service.AdminService;
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
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final TicketService ticketService;

    private final Logger log = LoggerFactory.getLogger(AdminController.class);

    // Only Admin can add roles
    @Operation(summary = "Add Role", description = "Only Admin can add roles")
    @PostMapping("/role")
    public ResponseEntity<RoleResponseDto> addRole(@RequestBody @Valid RoleRequestDto requestDto) {
        log.info("Request entered '/api/auth/role', addRole() method is called");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.addRole(requestDto));
    }

    // Get the Response for all the users of employee role
    @Operation(summary = "View All Employees", description = "Only Admin can get the Response for all the users of employee role")
    @GetMapping("/employees")
    public ResponseEntity<List<UserResponseDto>> findAllEmployees(@RequestParam int page, @RequestParam int size) {
        log.info("Request entered '/api/admin/employees', findAllEmployees() method is called");
        return ResponseEntity.ok(adminService.getAllEmployees(page, size));
    }

    // Get the Response for all the users of support agent role
    @Operation(summary = "View All Agents", description = "Only Admin can get the Response for all the users of support agent role")
    @GetMapping("/agents")
    public ResponseEntity<List<UserResponseDto>> findAllAgents(@RequestParam int page, @RequestParam int size) {
        log.info("Request entered '/api/admin/agents', findAllAgents() method is called");
        return ResponseEntity.ok(adminService.getAllAgents(page, size));
    }

    // Only Admin can view all the created tickets
    @Operation(summary = "View All Tickets", description = "Only Admin can view all created tickets")
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDto>> getAllTickets() {
        log.info("Request entered '/api/admin/tickets', getAllTickets() method is called");
        return ResponseEntity
                .ok(ticketService.findAllTickets());
    }

    // Only Admin can assign the ticket to agent
    @Operation(summary = "Assign Ticket", description = "Only Admin can assign the ticket to a agent")
    @PatchMapping("/ticket/{ticketId}/assign/{agentId}")
    public ResponseEntity<AssignTicketResponseDto> assignTicketToAgent(@PathVariable Long ticketId,
                                                                       @PathVariable Long agentId) {
        log.info("Request entered '/ticket/{}/assign/{}', assignTicketToAgent() method is called", ticketId, agentId);
        return ResponseEntity
                .ok(ticketService.assignTicketToAgent(ticketId, agentId));

    }

    // Admin can view the ticket summary
    @Operation(summary = "Ticket summary", description = "Admin can view the ticket summary such as number of tickets, ticket which are open, closed, resolved and in progress")
    @GetMapping("/ticket/summary")
    public ResponseEntity<TicketSummaryResponseDto> getTicketSummary() {
        log.info("Request entered '/ticket/summary', getTicketSummary() method is called");
        return ResponseEntity.ok(ticketService.getTicketSummary());
    }
}
