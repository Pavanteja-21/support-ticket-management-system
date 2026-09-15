package com.example.supportTicketManagement.dto;

import com.example.supportTicketManagement.enums.Priority;
import com.example.supportTicketManagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentTicketResponseDto {
    private String ticketNumber;

    private String title;

    private String description;

    private Priority priority;

    private Status status;
}
