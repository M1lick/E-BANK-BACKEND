package com.example.ebankbackend.services;

import com.example.ebankbackend.entities.Compte;
import com.example.ebankbackend.entities.CompteCourant;
import com.example.ebankbackend.entities.CompteEpargne;
import com.example.ebankbackend.repositories.CompteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private CompteRepository compteRepository;
    public  void consulter(){
        Compte compte=compteRepository.findById("4362d634-b17d-4ff1-8661-fc0213d82bd0").orElse(null);
        if(compte != null) {
            System.out.println("ID==>"+compte.getId());
            System.out.println("SOLDE==>"+compte.getSolde());
            System.out.println("STATUS==>"+compte.getStatusCompte());
            System.out.println("CLIEN=t=>"+compte.getClient().getName());
            System.out.println("DATE DE CREATION==>"+compte.getDateCreation());
            if (compte instanceof CompteCourant){
                System.out.println("Decouverte==>"+((CompteCourant) compte).getDecouvert());
            }
            else {
                System.out.println("Taux==>"+((CompteEpargne) compte).getTauxInteret());
            }

            compte.getOperations().forEach(op -> {
                System.out.println(op.getDateOperation() +"\t"+op.getTypeOperation() +"\t"+op.getMontant());
            });

        }
    }
}
