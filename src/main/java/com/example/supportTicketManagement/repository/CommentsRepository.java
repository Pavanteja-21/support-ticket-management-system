package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment,Long> {

}
