package com.example.supportTicketManagement.service;

import com.example.supportTicketManagement.dto.CommentRequestDto;
import com.example.supportTicketManagement.dto.CommentResponseDto;
import com.example.supportTicketManagement.entity.Comment;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.TicketNotFoundException;
import com.example.supportTicketManagement.repository.CommentsRepository;
import com.example.supportTicketManagement.repository.TicketRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.impl.CommentServiceImpl;
import com.example.supportTicketManagement.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CommentsRepository commentsRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User employee;
    private User agent;
    private Ticket ticket;
    private Comment comment;
    private CommentResponseDto  commentResponseDto;
    private CommentRequestDto commentRequestDto;

    // This is the predefined setup required to execute in all test cases
    @BeforeEach
    void setup() {
        employee = new User();
        employee.setId(1L);

        employee.setEmail("deva@gmail.com");

        agent = new User();
        agent.setId(2L);
        agent.setEmail("satti@gmail.com");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEmployee(employee);
        ticket.setAgent(agent);

        commentRequestDto = new CommentRequestDto();
        commentRequestDto.setComment("Hi");

        commentResponseDto = new CommentResponseDto();
        commentResponseDto.setComment(commentRequestDto.getComment());

    }

    // This passes if Employee and Ticket is found on db
    @Test
    void shouldEmployeeAbleToCreateComment() {
        comment = new Comment();
        comment.setId(1L);
        comment.setUser(employee);
        comment.setTicket(ticket);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("deva@gmail.com");


        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        when(ticketRepository.findByIdAndEmployeeId(1L, employee.getId())).thenReturn(Optional.of(ticket));
        when(commentsRepository.save(any(Comment.class))).thenReturn(comment);
        when(mapper.mapToCommentDto(comment)).thenReturn(commentResponseDto);

        CommentResponseDto result = commentService.createEmployeeComment(commentRequestDto, ticket.getId());

        assertNotNull(result);
        assertEquals(commentResponseDto.getComment(), result.getComment());

        verify(userRepository).findByEmail(employee.getEmail());
        verify(ticketRepository).findByIdAndEmployeeId(1L, employee.getId());
        verify(commentsRepository).save(any(Comment.class));
    }

    // This passes if Agent and Ticket is found on db
    @Test
    void shouldAgentAbleToCreateComment() {
        comment = new Comment();
        comment.setId(1L);
        comment.setUser(agent);
        comment.setTicket(ticket);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("satti@gmail.com");


        when(userRepository.findByEmail(agent.getEmail())).thenReturn(Optional.of(agent));
        when(ticketRepository.findByIdAndEmployeeId(1L, agent.getId())).thenReturn(Optional.of(ticket));
        when(commentsRepository.save(any(Comment.class))).thenReturn(comment);
        when(mapper.mapToCommentDto(comment)).thenReturn(commentResponseDto);

        CommentResponseDto result = commentService.createEmployeeComment(commentRequestDto,ticket.getId() );

        assertNotNull(result);
        assertEquals(commentResponseDto.getComment(), result.getComment());


        verify(userRepository).findByEmail(agent.getEmail());
        verify(ticketRepository).findByIdAndEmployeeId(1L, agent.getId());
        verify(commentsRepository).save(any(Comment.class));
    }

    // Test case passes if it throws UsernameNotFoundException
    @Test
    void shouldAbleThrowUsernameNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("deva@gmail.com");

        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> commentService.createEmployeeComment(commentRequestDto, ticket.getId())
        );

        assertEquals("Username not found", ex.getMessage());
        verify(ticketRepository, never()).findByIdAndEmployeeId(anyLong(), anyLong());
        verify(commentsRepository, never()).save(any(Comment.class));
    }

    // Test case passes if it throws TicketNotFoundException
    @Test
    void shouldAbleToThrowTicketNotFoundException(){
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("deva@gmail.com");

        when(userRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        when(ticketRepository.findByIdAndEmployeeId(1L, employee.getId())).thenReturn(Optional.empty());

        TicketNotFoundException ex = assertThrows(
                TicketNotFoundException.class,
                () -> commentService.createEmployeeComment(commentRequestDto, ticket.getId())
        );

        assertEquals("Ticket not found", ex.getMessage());
        verify(commentsRepository, never()).save(any(Comment.class));
    }
}
