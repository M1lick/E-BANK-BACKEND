package com.example.ebankbackend.repositories;

import com.example.ebankbackend.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte,String> {
}
