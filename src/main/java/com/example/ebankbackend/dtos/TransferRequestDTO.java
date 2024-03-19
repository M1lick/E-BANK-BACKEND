package com.example.ebankbackend.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String compteSource;
    private  String compteDestination;
    private  double montant;
    private  String description;
}
