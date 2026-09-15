package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    // Gets all the tickets by employeeId
    List<Ticket> findByEmployeeId(Long userId);
}
