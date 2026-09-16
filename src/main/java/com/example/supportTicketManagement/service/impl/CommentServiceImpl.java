package com.example.supportTicketManagement.service.impl;

import com.example.supportTicketManagement.dto.CommentRequestDto;
import com.example.supportTicketManagement.dto.CommentResponseDto;
import com.example.supportTicketManagement.entity.Comment;
import com.example.supportTicketManagement.entity.Ticket;
import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.TicketNotFoundException;
import com.example.supportTicketManagement.repository.CommentsRepository;
import com.example.supportTicketManagement.repository.TicketRepository;
import com.example.supportTicketManagement.repository.UserRepository;
import com.example.supportTicketManagement.service.CommentService;
import com.example.supportTicketManagement.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    // Used to create a comment to ticket created by employee
    @Override
    public CommentResponseDto createEmployeeComment(CommentRequestDto requestDto, Long ticketId) {
        log.info("Inside CommentService.createEmployeeComment() method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found with email in CommentService.createEmployeeComment()");
                    return new UsernameNotFoundException("Username not found");
                });

        Ticket ticket = ticketRepository.findByIdAndEmployeeId(ticketId, user.getId())
                .orElseThrow(() -> {
                    log.info("Threw TikcetNotFoundException as ticket is not found in CommentService.createEmployeeComment()");
                    return new TicketNotFoundException("Ticket not found");
                });

        Comment comment = new Comment();
        comment.setComment(requestDto.getComment());
        comment.setTicket(ticket);
        comment.setUser(user);

        Comment savedComment = commentsRepository.save(comment);

        log.info("Successfully saved the comment in db, end of the AuthService.createEmployeeComment() method");

        return mapper.mapToCommentDto(savedComment);
    }

    // Used to create a comment to ticket that is assigned to agent
    @Override
    public CommentResponseDto createAgentComment(CommentRequestDto requestDto, Long ticketId) {
        log.info("Inside CommentService.createAgentComment() method");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.info("Threw UsernameNotFoundException as user is not found with email in CommentService.createAgentComment()");
                    return new UsernameNotFoundException("Username not found");
                });

        Ticket ticket = ticketRepository.findByIdAndAgentId(ticketId, user.getId())
                .orElseThrow(() -> {
                    log.info("Threw TikcetNotFoundException as ticket is not found in CommentService.createAgentComment()");
                    return new TicketNotFoundException("Ticket not found");
                });

        Comment comment = new Comment();
        comment.setComment(requestDto.getComment());
        comment.setTicket(ticket);
        comment.setUser(user);

        Comment savedComment = commentsRepository.save(comment);

        log.info("Successfully saved the comment in db, end of the AuthService.createAgentComment() method");

        return mapper.mapToCommentDto(savedComment);
    }
}
