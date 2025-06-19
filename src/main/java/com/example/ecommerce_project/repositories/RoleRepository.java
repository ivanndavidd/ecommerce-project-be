package com.example.ecommerce_project.repositories;

import com.example.ecommerce_project.model.AppRole;
import com.example.ecommerce_project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole role);
}
