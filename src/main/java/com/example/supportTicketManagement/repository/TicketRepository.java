package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
