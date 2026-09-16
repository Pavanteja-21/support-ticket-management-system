package com.example.supportTicketManagement.utils;

import com.example.supportTicketManagement.dto.*;
import com.example.supportTicketManagement.entity.Comment;
import com.example.supportTicketManagement.entity.Role;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Mapper {

    // This method is used to map the User entity to RegisterResponseDto
    public UserResponseDto mapToUserRegisterDto(User user) {

        Set<RoleResponseDto> roles = user.getRoles()
                .stream()
                .map(role -> new RoleResponseDto(role.getId(),role.getRoleName()))
                .collect(Collectors.toSet());

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setEmail(user.getEmail());
        responseDto.setActive(user.isActive());
        responseDto.setRoles(roles);

        return responseDto;
    }

    // This method is used to map the Role entity to RoleResponseDto
    public RoleResponseDto mapToRoleDto(Role role) {
        RoleResponseDto responseDto = new RoleResponseDto();
        responseDto.setRoleId(role.getId());
        responseDto.setRoleName(role.getRoleName());
        return responseDto;
    }

    // This method is used to map the Ticket entity to CreateTicketResponseDto
    public CreateTicketResponseDto mapToCreateTicket(Ticket ticket) {
        CreateTicketResponseDto responseDto = new CreateTicketResponseDto();
        responseDto.setId(ticket.getId());
        responseDto.setTicketNumber(ticket.getTicketNumber());
        responseDto.setTitle(ticket.getTitle());
        responseDto.setDescription(ticket.getDescription());
        responseDto.setPriority(ticket.getPriority());
        responseDto.setStatus(ticket.getStatus());
        responseDto.setCreatedAt(ticket.getCreatedAt());
        responseDto.setEmployee(mapToUserRegisterDto(ticket.getEmployee()));

        return responseDto;
    }

    // This method is used to map the Ticket entity to AssignTicketResponseDto
    public AssignTicketResponseDto mapToAssignTicket(Ticket ticket) {
        AssignTicketResponseDto responseDto = new AssignTicketResponseDto();
        responseDto.setTicketId(ticket.getId());
        responseDto.setTicketNumber(ticket.getTicketNumber());
        responseDto.setAgent(mapToUserRegisterDto(ticket.getAgent()));
        responseDto.setStatus(ticket.getStatus());

        return responseDto;
    }

    // This method is used to map the Ticket entity to AgentTicketResponseDto
    public AgentTicketResponseDto mapToAgentTicket(Ticket ticket) {
        AgentTicketResponseDto responseDto = new AgentTicketResponseDto();
        responseDto.setId(ticket.getId());
        responseDto.setTicketNumber(ticket.getTicketNumber());
        responseDto.setTitle(ticket.getTitle());
        responseDto.setDescription(ticket.getDescription());
        responseDto.setPriority(ticket.getPriority());
        responseDto.setStatus(ticket.getStatus());

        return responseDto;
    }

    // This method is used to map the Ticket entity to TicketStatusResponseDto
    public TicketStatusResponseDto mapToTicketStatusDto(Ticket ticket) {
        TicketStatusResponseDto responseDto = new TicketStatusResponseDto();
        responseDto.setId(ticket.getId());
        responseDto.setTicketNumber(ticket.getTicketNumber());
        responseDto.setTitle(ticket.getTitle());
        responseDto.setDescription(ticket.getDescription());
        responseDto.setPriority(ticket.getPriority());
        responseDto.setStatus(ticket.getStatus());
        responseDto.setUpdatedAt(ticket.getUpdatedAt());
        responseDto.setEmployee(mapToUserRegisterDto(ticket.getEmployee()));

        return responseDto;
    }

    // This method is used to map the Comment entity to CommentResponseDto
    public CommentResponseDto mapToCommentDto(Comment comment) {
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(comment.getId());
        responseDto.setComment(comment.getComment());
        responseDto.setCreatedAt(comment.getCreatedAt());
        responseDto.setTicket(mapToTicketStatusDto(comment.getTicket()));
        responseDto.setUser(mapToUserRegisterDto(comment.getUser()));

        return responseDto;
    }
    // This method is used to map the Ticket entity to TicketResponseDto
    public TicketResponseDto mapToTicketResponseDto(Ticket ticket) {
        TicketResponseDto responseDto = new TicketResponseDto();
        responseDto.setId(ticket.getId());
        responseDto.setTicketNumber(ticket.getTicketNumber());
        responseDto.setTitle(ticket.getTitle());
        responseDto.setDescription(ticket.getDescription());
        responseDto.setPriority(ticket.getPriority());
        responseDto.setStatus(ticket.getStatus());
        responseDto.setCreatedAt(ticket.getCreatedAt());
        responseDto.setUpdatedAt(ticket.getUpdatedAt());
        responseDto.setEmployee(ticket.getEmployee().getFirstName() + " " + ticket.getEmployee().getLastName());
        responseDto.setAgent(ticket.getAgent().getFirstName() + " " + ticket.getAgent().getLastName());

        return responseDto;
    }

}
