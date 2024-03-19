package com.example.ebankbackend.entities;

import com.example.ebankbackend.enums.StatusCompte;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Data @NoArgsConstructor @AllArgsConstructor
public class Compte {
    @Id
    private String id;
    private double solde ;
    private Date dateCreation;
    @Enumerated(EnumType.STRING)
    private StatusCompte statusCompte;
    @ManyToOne
    private  Client client;
    @OneToMany(mappedBy = "compte" ,fetch = FetchType.LAZY)
    private List<Operation> operations;
}
