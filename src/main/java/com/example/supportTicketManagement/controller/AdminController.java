package com.example.supportTicketManagement.controller;

import com.example.supportTicketManagement.dto.UserResponseDto;
import com.example.supportTicketManagement.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

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
}
