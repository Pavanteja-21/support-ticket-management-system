package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.CommentRequestDto;
import com.example.supportTicketManagement.dto.CommentResponseDto;
import com.example.supportTicketManagement.entity.Comment;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.TicketNotFoundException;
import com.example.supportTicketManagement.exception.UserNotFoundException;
import com.example.supportTicketManagement.repository.CommentsRepository;
import com.example.supportTicketManagement.repository.TicketRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.CommentService;
import com.example.supportTicketManagement.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentsRepository commentsRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    // Used to create a comment to ticket created by employee
    @Override
    public CommentResponseDto createEmployeeComment(CommentRequestDto requestDto, Long ticketId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Username not found"));

        Ticket ticket = ticketRepository.findByIdAndEmployeeId(ticketId, user.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        Comment comment = new Comment();
        comment.setComment(requestDto.getComment());
        comment.setTicket(ticket);
        comment.setUser(user);

        Comment savedComment = commentsRepository.save(comment);

        return mapper.mapToCommentDto(savedComment);
    }

    // Used to create a comment to ticket that is assigned to agent
    @Override
    public CommentResponseDto createAgentComment(CommentRequestDto requestDto, Long ticketId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Username not found"));

        Ticket ticket = ticketRepository.findByIdAndAgentId(ticketId, user.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        Comment comment = new Comment();
        comment.setComment(requestDto.getComment());
        comment.setTicket(ticket);
        comment.setUser(user);

        Comment savedComment = commentsRepository.save(comment);

        return mapper.mapToCommentDto(savedComment);
    }
}
