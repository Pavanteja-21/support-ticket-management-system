package com.example.supportTicketManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketSummaryResponseDto {
    private long totalTickets;

    private long openTickets;

    private long inProgressTickets;

    private long resolvedTickets;

    private long closedTickets;
}
