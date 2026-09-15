package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.CreateTicketResponseDto;
import com.example.supportTicketManagement.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final TicketService ticketService;

    // Gets all tickets created by employee
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/mytickets")
    public ResponseEntity<List<CreateTicketResponseDto>> getAllMyTickets() {
        return ResponseEntity
                .ok(ticketService.findAllMyTicktets());
    }
}
