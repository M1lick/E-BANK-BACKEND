package com.example.ebankbackend.dtos;

import com.example.ebankbackend.enums.StatusCompte;
import lombok.Data;

import java.util.Date;

@Data
public class CompteCourantDTO extends CompteDTO {
    private String id;
    private double solde ;
    private Date dateCreation;
    private StatusCompte statusCompte;
    private ClientDTO clientDTO;
    private double decouvert;
}
