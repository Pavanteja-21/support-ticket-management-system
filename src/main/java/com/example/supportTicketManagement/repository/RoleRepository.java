package com.example.supportTicketManagement.repository;

import com.example.supportTicketManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(String name);

    Boolean existsByRoleName(String name);
}
