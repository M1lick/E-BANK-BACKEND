package com.example.ebankbackend.repositories;

import com.example.ebankbackend.entities.ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRoleRepository extends JpaRepository<ClientRole,String> {

    ClientRole findByRole(String role);
}
