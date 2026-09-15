package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("""
        SELECT u
        FROM User u
        JOIN u.roles r
        WHERE r.roleName = :roleName
    """)
    List<User> findUsersByRole(@Param("roleName") String roleName, Pageable pageable);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN u.roles r
        WHERE u.email = :email
          AND r.roleName = 'EMPLOYEE'
        """)
    Optional<User> findEmployeeByEmail(@Param("email") String email);
}
