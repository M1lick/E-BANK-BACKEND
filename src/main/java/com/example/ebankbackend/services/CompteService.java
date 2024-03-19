package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.*;
import com.example.ebankbackend.entities.Client;
import com.example.ebankbackend.entities.Compte;
import com.example.ebankbackend.entities.CompteCourant;
import com.example.ebankbackend.entities.CompteEpargne;
import com.example.ebankbackend.exceptions.ClientNonTrouve;
import com.example.ebankbackend.exceptions.CompteNonTrouverException;
import com.example.ebankbackend.exceptions.SoldeInsuffisantException;

import java.util.List;

public interface CompteService {
    ClientDTO creerClient(ClientDTO clientDTO);
    CompteCourantDTO creerCompteCourant(double soldeInitiale, double decouver, Long IdClient) throws ClientNonTrouve;
    CompteEpargneDTO creerCompteEpargne(double soldeInitiale, double tauxInteret, Long IdClient) throws ClientNonTrouve;
    List<ClientDTO> listesClients();
    CompteDTO getCompte(String IdCompte) throws CompteNonTrouverException;
    void retrait(String IdCompte,double momtant ,String description) throws CompteNonTrouverException, SoldeInsuffisantException;
    void depot(String IdCompte,double momtant ,String description) throws CompteNonTrouverException;
    void transfer(String IdCompteSource,String IdCompteDestination,double momtant ) throws SoldeInsuffisantException, CompteNonTrouverException;


    List<CompteDTO> listComptes();

    ClientDTO getClient(Long idClient) throws ClientNonTrouve;

    ClientDTO updateClient(ClientDTO client);

    void deleteClient(Long idClient);

    List<OperationDTO> getOperations(String idCompte);

    OperationsHistoryDTO getHistoryOperations(String idCompte, int page, int size) throws CompteNonTrouverException;

    List<ClientDTO> searchClient(String keyword);
}
