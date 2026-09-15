package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    // Returns the role by role name
    Optional<Role> findByRoleName(String name);

    // Checks whether the role is exists in db
    Boolean existsByRoleName(String name);
}
