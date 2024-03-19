package com.example.ebankbackend.dtos;

import com.example.ebankbackend.entities.Compte;
import com.example.ebankbackend.enums.TypeOperation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date dateOperation;
    private double montant;
    private TypeOperation typeOperation;
    private  String description;
}
