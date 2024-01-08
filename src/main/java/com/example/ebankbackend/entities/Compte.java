package com.example.ebankbackend.entities;

import com.example.ebankbackend.enums.StatusCompte;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Compte {
    @Id
    private String id;
    private double solde ;
    private Date dateCreation;
    private StatusCompte statusCompte;
    @ManyToOne
    private  Client client;
    @OneToMany(mappedBy = "compte")
    private List<Operation> operations;
}
