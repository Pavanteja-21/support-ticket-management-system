package com.example.supportTicketManagement.exception;

public class TicketCannotClosedException extends RuntimeException {
    public TicketCannotClosedException(String message) {
        super(message);
    }
}
