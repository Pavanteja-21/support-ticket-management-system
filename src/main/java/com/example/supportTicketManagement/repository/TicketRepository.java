package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    // Gets all the tickets by employeeId
    List<Ticket> findByEmployeeId(Long userId);

    // Gets all the tickets by agentId
    List<Ticket> findByAgentId(Long agentId);

    // Gets the ticket by ticket id and agent id
    Optional<Ticket> findByIdAndAgentId(Long id, Long agentId);

    // Gets the ticket by ticket id and employee id
    Optional<Ticket> findByIdAndEmployeeId(Long id, Long employeeId);

    // Gets the count of Ticket By Status
    long countByStatus(Status status);
}
