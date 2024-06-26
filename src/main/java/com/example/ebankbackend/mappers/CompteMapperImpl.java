package com.example.ebankbackend.mappers;

import com.example.ebankbackend.dtos.*;
import com.example.ebankbackend.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteMapperImpl {
    public ClientDTO fromClient(Client client){
        ClientDTO clientDTO=new ClientDTO();
        BeanUtils.copyProperties(client,clientDTO);
        return clientDTO;
    }
    public Client fromClientDTO(ClientDTO clientDTO){
        Client client=new Client();
        BeanUtils.copyProperties(clientDTO,client);
        return client;
    }
    public ClientRoleDTO fromClientRole(ClientRole clientRole){
        ClientRoleDTO clientRoleDTO=new ClientRoleDTO();
        BeanUtils.copyProperties(clientRole,clientRoleDTO);
        return clientRoleDTO;
    }
    public ClientRole fromClientRoleDTO(ClientRoleDTO clientRoleDTO){
        ClientRole clientRole=new ClientRole();
        BeanUtils.copyProperties(clientRoleDTO,clientRole);
        return clientRole;
    }
    public CompteEpargneDTO fromCompteEpargne(CompteEpargne compteEpargne){
        CompteEpargneDTO compteEpargneDTO=new CompteEpargneDTO();
        BeanUtils.copyProperties(compteEpargne,compteEpargneDTO);
        compteEpargneDTO.setClientDTO(fromClient(compteEpargne.getClient()));
        compteEpargneDTO.setType(compteEpargne.getClass().getSimpleName());
        return compteEpargneDTO ;


    }
    public CompteEpargne fromCompteEpargneDTO(CompteEpargneDTO compteEpargneDTO){
        CompteEpargne compteEpargne=new CompteEpargne();
        BeanUtils.copyProperties(compteEpargneDTO,compteEpargne);
        compteEpargne.setClient(fromClientDTO(compteEpargneDTO.getClientDTO()));
        return compteEpargne;

    }
    public CompteCourantDTO fromCompteCourant(CompteCourant compteCourant){
        CompteCourantDTO  compteCourantDTO=new CompteCourantDTO();
        BeanUtils.copyProperties(compteCourant,compteCourantDTO);
        compteCourantDTO.setClientDTO(fromClient(compteCourant.getClient()));
        compteCourantDTO.setType(compteCourant.getClass().getSimpleName());
        return compteCourantDTO;

    }
    public CompteCourant fromCompteCourantDTO(CompteCourantDTO compteCourantDTO){
        CompteCourant compteCourant=new CompteCourant();
        BeanUtils.copyProperties(compteCourantDTO,compteCourant);
        compteCourant.setClient(fromClientDTO(compteCourantDTO.getClientDTO()));
        return compteCourant;

    }
    public OperationDTO fromOpreraton(Operation operation){
        OperationDTO operationDTO=new OperationDTO();
        BeanUtils.copyProperties(operation,operationDTO);
        return operationDTO;

    }
}
