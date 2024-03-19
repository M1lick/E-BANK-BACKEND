package com.example.ebankbackend.dtos;

import lombok.Data;

@Data
public class DepotDTO {
    private String compteId;
    private  double montant;
    private  String description;
}
