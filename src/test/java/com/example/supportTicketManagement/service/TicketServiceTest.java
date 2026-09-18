package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.CreateTicketRequestDto;
import com.example.supportTicketManagement.dto.CreateTicketResponseDto;

import com.example.supportTicketManagement.dto.TicketResponseDto;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.enums.Priority;
import com.example.supportTicketManagement.enums.Status;
import com.example.supportTicketManagement.repository.TicketRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.impl.TicketServiceImpl;
import com.example.supportTicketManagement.utils.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Mapper mapper;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User employee;
    private User agent;
    private Ticket ticket;
    private CreateTicketResponseDto createTicketResponseDto;

    // This is the predefined setup required to execute in all test cases
    @BeforeEach
    void setup() {
        employee = new User();
        employee.setId(1L);
        employee.setFirstName("Deva");
        employee.setLastName("Konda");
        employee.setEmail("deva@gmail.com");

        agent = new User();
        agent.setId(2L);
        agent.setFirstName("Satti");
        agent.setLastName("Tatapudi");
        agent.setEmail("satti@gmail.com");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEmployee(employee);
        ticket.setAgent(agent);
        ticket.setTicketNumber("TKT-0001");
        ticket.setDescription("Hi");
        ticket.setTitle("Dummy");
        ticket.setPriority(Priority.HIGH);

        createTicketResponseDto = new CreateTicketResponseDto();
        createTicketResponseDto.setTicketId(ticket.getId());
        createTicketResponseDto.setTicketNumber(ticket.getTicketNumber());
        createTicketResponseDto.setStatus(Status.OPEN);
        createTicketResponseDto.setDescription(ticket.getDescription());
        createTicketResponseDto.setTitle(ticket.getTitle());

    }

    // This is the cleanup for clearing SecurityContextHolder after executing all test cases.
    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    // It passes if Employee is able to create ticket and save in db
    @Test
    void employeeShouldAbleToCreateTicket() {
        CreateTicketRequestDto requestDto = new CreateTicketRequestDto();
        requestDto.setTitle("Dummy");
        requestDto.setDescription("Hi");
        requestDto.setPriority(Priority.HIGH);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(employee.getEmail());

        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(mapper.mapToCreateTicket(ticket)).thenReturn(createTicketResponseDto);

        CreateTicketResponseDto result = ticketService.createTicket(requestDto);

        assertNotNull(result);
        assertEquals(createTicketResponseDto.getTicketId(), result.getTicketId());
        assertEquals(createTicketResponseDto.getTicketNumber(), result.getTicketNumber());
        assertEquals(createTicketResponseDto.getDescription(), result.getDescription());
        assertEquals(createTicketResponseDto.getTitle(), result.getTitle());
        assertEquals(createTicketResponseDto.getPriority(), result.getPriority());
        assertEquals(createTicketResponseDto.getStatus(), result.getStatus());

        verify(userRepository).findByEmail(employee.getEmail());
        verify(ticketRepository).save(any(Ticket.class));
        verify(mapper).mapToCreateTicket(ticket);
    }

    // Test cases passes if all tickets are returned from db
    @Test
    void shouldReturnAllTickets() {
        TicketResponseDto response1 = new TicketResponseDto();
        response1.setId(ticket.getId());
        response1.setTicketNumber(ticket.getTicketNumber());
        response1.setDescription(ticket.getDescription());
        response1.setTitle(ticket.getTitle());
        response1.setPriority(ticket.getPriority());

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setTicketNumber("TKT-0002");
        ticket1.setDescription("Hi");
        ticket1.setTitle("Dummy");
        ticket1.setPriority(Priority.HIGH);

        TicketResponseDto response2 = new TicketResponseDto();
        response2.setId(ticket1.getId());
        response2.setTicketNumber(ticket1.getTicketNumber());
        response2.setDescription(ticket1.getDescription());
        response2.setTitle(ticket1.getTitle());
        response2.setPriority(ticket1.getPriority());

        List<Ticket> tickets = List.of(ticket, ticket1);

        when(ticketRepository.findAll()).thenReturn(tickets);
        when(mapper.mapToTicketResponseDto(ticket)).thenReturn(response1);
        when(mapper.mapToTicketResponseDto(ticket1)).thenReturn(response2);

        List<TicketResponseDto> result = ticketService.findAllTickets();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.getFirst());
        assertEquals(response2, result.getLast());

        verify(ticketRepository).findAll();
        verify(mapper).mapToTicketResponseDto(ticket);
        verify(mapper).mapToTicketResponseDto(ticket1);
    }
}
