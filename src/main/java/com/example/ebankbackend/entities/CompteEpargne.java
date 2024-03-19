package com.example.ebankbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@DiscriminatorValue("CE")
@Data @NoArgsConstructor @AllArgsConstructor
public class CompteEpargne extends Compte{
    private double tauxInteret;
}
