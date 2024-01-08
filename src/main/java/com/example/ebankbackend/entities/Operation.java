package com.example.ebankbackend.entities;

import com.example.ebankbackend.enums.TypeOperation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateOperation;
    private double montant;
    private TypeOperation typeOperation;
    @ManyToOne
    private Compte compte;
}
