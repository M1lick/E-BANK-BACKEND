package com.example.ebankbackend.dtos;

import lombok.Data;

@Data
public class RetraitDTO {
    private String compteId;
    private  double montant;
    private  String description;
}
