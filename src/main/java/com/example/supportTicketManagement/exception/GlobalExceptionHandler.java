package com.example.supportTicketManagement.exception;

import com.example.supportTicketManagement.dto.ExceptionResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles the UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUsernameNotFoundException
            (UsernameNotFoundException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    // Handles the BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException
            (BadCredentialsException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseDto);
    }

    // Handles the EmailAlreadyExistsException
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDto> handleEmailAlreadyExistsException
            (EmailAlreadyExistsException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

    // Handles the AuthorizationDeniedException
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponseDto> handleAuthorizationDeniedException
            (AuthorizationDeniedException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(responseDto);
    }

    // Handles the RoleAlreadyExistsException
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDto> handleRoleAlreadyExistsException
    (RoleAlreadyExistsException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

    // Handles the RoleNotFoundException
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleRoleNotFoundException
    (RoleNotFoundException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    // Handles the TicketClosedException
    @ExceptionHandler(TicketClosedException.class)
    public ResponseEntity<ExceptionResponseDto> handleTicketClosedException
    (TicketClosedException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

    // Handles the TicketCannotClosedException
    @ExceptionHandler(TicketCannotClosedException.class)
    public ResponseEntity<ExceptionResponseDto> handleTicketCannotClosedException
    (TicketCannotClosedException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

    // Handles the TicketNotFoundException
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleTicketNotFoundException
    (TicketNotFoundException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    // Handles the TicketNotFoundException
    @ExceptionHandler(AgentNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleAgentNotFoundException
    (AgentNotFoundException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    // Handles the SamePasswordException
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ExceptionResponseDto> handleSamePasswordException
    (PasswordMismatchException ex, HttpServletRequest request) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

}
