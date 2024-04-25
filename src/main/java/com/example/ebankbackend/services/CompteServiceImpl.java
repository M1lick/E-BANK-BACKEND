package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.*;
import com.example.ebankbackend.entities.*;
import com.example.ebankbackend.enums.TypeOperation;
import com.example.ebankbackend.exceptions.ClientNonTrouve;
import com.example.ebankbackend.exceptions.CompteNonTrouverException;
import com.example.ebankbackend.exceptions.SoldeInsuffisantException;
import com.example.ebankbackend.mappers.CompteMapperImpl;
import com.example.ebankbackend.repositories.ClientRepository;
import com.example.ebankbackend.repositories.ClientRoleRepository;
import com.example.ebankbackend.repositories.CompteRepository;
import com.example.ebankbackend.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

public class CompteServiceImpl implements CompteService{
    private ClientRoleRepository clientRoleRepository;
    private CompteRepository compteRepository;
    private ClientRepository clientRepository;
    private OperationRepository operationRepository;
    private CompteMapperImpl dtoMapper;
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    public ClientDTO creerClient(ClientDTO client) {
        log.info("enregistrement nouveau client");
        Client clientCreer=dtoMapper.fromClientDTO(client);
        clientCreer.setPassword(passwordEncoder().encode(clientCreer.getPassword()));
        clientRepository.save(clientCreer);
        return dtoMapper.fromClient(clientCreer);

    }
    public ClientRoleDTO addnewRole(String role) {
        // Créer un nouveau rôle
        ClientRole clientRole = new ClientRole(role);
        // Enregistrer le rôle dans la base de données
        ClientRole savedRole = clientRoleRepository.save(clientRole);
        // Convertir et retourner le rôle sauvegardé en ClientRoleDTO
        return dtoMapper.fromClientRole(savedRole);
    }

    @Override
    public void addRoleToClient(String email, String role) throws ClientNonTrouve {
        // Récupérer le client par email
        Client client = clientRepository.findByEmail(email) ;
        if (client==null)
            new ClientNonTrouve("Client non trouvé avec l'email: " + email);
        // Récupérer le rôle par son nom
        ClientRole clientRole = clientRoleRepository.findByRole(role);
        if (clientRole == null) {
            // Si le rôle n'existe pas, créer un nouveau rôle
            clientRole = new ClientRole(role);
            clientRole = clientRoleRepository.save(clientRole);
        }
        // Ajouter le rôle au client
        client.getRole().add(clientRole);
        // Mettre à jour le client dans la base de données
        clientRepository.save(client);
    }

    @Override
    public void deletRoleFromClient(String email, String role) throws ClientNonTrouve {
        // Récupérer le client par email
        Client client = clientRepository.findByEmail(email);
        if(client==null)
            new ClientNonTrouve("Client non trouvé avec l'email: " + email);
        // Récupérer le rôle par son nom
        ClientRole clientRole = clientRoleRepository.findByRole(role);
        if (clientRole != null) {
            // Supprimer le rôle du client
            client.getRole().remove(clientRole);
            // Mettre à jour le client dans la base de données
            clientRepository.save(client);
        }
    }
    @Override
    public CompteCourantDTO creerCompteCourant(double soldeInitiale, double decouver, Long IdClient) throws ClientNonTrouve {
        Client client=clientRepository.findById(IdClient).orElse(null);
        if(client==null)
            throw new ClientNonTrouve("client non trouve(e)");
        CompteCourant compteCourant=new CompteCourant();

        compteCourant.setId(UUID.randomUUID().toString());
        compteCourant.setDateCreation(new Date());
        compteCourant.setSolde(soldeInitiale);
        compteCourant.setClient(client);
        compteCourant.setDecouvert(decouver);
        CompteCourant cc=compteRepository.save(compteCourant);

        return dtoMapper.fromCompteCourant(cc);
    }

    @Override
    public CompteEpargneDTO creerCompteEpargne(double soldeInitiale, double tauxInteret, Long IdClient) throws ClientNonTrouve {
        Client client=clientRepository.findById(IdClient).orElse(null);
        if(client==null)
            throw new ClientNonTrouve("client non trouve(e)");
        CompteEpargne compteEpargne=new CompteEpargne();

        compteEpargne.setId(UUID.randomUUID().toString());
        compteEpargne.setDateCreation(new Date());
        compteEpargne.setSolde(soldeInitiale);
        compteEpargne.setClient(client);
        compteEpargne.setTauxInteret(tauxInteret);
        CompteEpargne ce=compteRepository.save(compteEpargne);

        return dtoMapper.fromCompteEpargne(ce);
    }


    @Override
    public List<ClientDTO> listesClients() {
        List<Client> clients=clientRepository.findAll();
        List<ClientDTO> clientDTOS = clients.stream()
                .map(client -> dtoMapper.fromClient(client))
                .collect(Collectors.toList());
        return clientDTOS;
    }

    @Override
    public CompteDTO getCompte(String IdCompte) throws CompteNonTrouverException {
        Compte compte=compteRepository.findById(IdCompte)
                .orElseThrow(()->new CompteNonTrouverException("compte non trouver"));
        if (compte instanceof CompteEpargne){
            CompteEpargne compteEpargne= (CompteEpargne) compte;
            return dtoMapper.fromCompteEpargne(compteEpargne);
        }else{
            CompteCourant compteCourant= (CompteCourant) compte;
            return  dtoMapper.fromCompteCourant(compteCourant);
        }

    }

    @Override
    public void retrait(String IdCompte, double momtant, String description) throws CompteNonTrouverException, SoldeInsuffisantException {
        Compte compte=compteRepository.findById(IdCompte)
                .orElseThrow(()->new CompteNonTrouverException("compte non trouver"));
        if(compte.getSolde()<momtant)
            throw new SoldeInsuffisantException("solde in suffisant");
        Operation operation=new Operation();
        operation.setTypeOperation(TypeOperation.DEBITER);
        operation.setMontant(momtant);
        operation.setDescription(description);
        operation.setDateOperation(new Date());
        operation.setCompte(compte);
        operationRepository.save(operation);
        compte.setSolde(compte.getSolde()-momtant);
        compteRepository.save(compte);
    }

    @Override
    public void depot(String IdCompte, double momtant, String description) throws CompteNonTrouverException {
        Compte compte=compteRepository.findById(IdCompte)
                .orElseThrow(()->new CompteNonTrouverException("compte non trouver"));
        Operation operation = new Operation();
        operation.setTypeOperation(TypeOperation.CREDITER);
        operation.setMontant(momtant);
        operation.setDescription(description);
        operation.setDateOperation(new Date());
        operation.setCompte(compte);
        operationRepository.save(operation);
        compte.setSolde(compte.getSolde() + momtant);
        compteRepository.save(compte);

    }

    @Override
    public void transfer(String IdCompteSource, String IdCompteDestination, double momtant) throws SoldeInsuffisantException, CompteNonTrouverException {
        retrait(IdCompteSource,momtant,"Transfere pour : "+IdCompteDestination);
        depot(IdCompteDestination,momtant,"Transfer venant : "+IdCompteSource);

    }
 @Override
    public List<CompteDTO> listComptes(){
     List<Compte> comptes = compteRepository.findAll();
     List<CompteDTO> compteDTOList = comptes.stream().map(compte -> {
         if (compte instanceof CompteCourant) {
             CompteCourant compteCourant = (CompteCourant) compte;
             return dtoMapper.fromCompteCourant(compteCourant);
         } else {
             CompteEpargne compteEpargne = (CompteEpargne) compte;
             return dtoMapper.fromCompteEpargne(compteEpargne);
         }

     }).collect(Collectors.toList());
     return compteDTOList;

 }
 @Override
 public ClientDTO getClient(Long idClient) throws ClientNonTrouve {
     Client client = clientRepository.findById(idClient).
             orElseThrow(() -> new ClientNonTrouve("client non touver"));
     return dtoMapper.fromClient(client);
 }
 @Override
 public ClientDTO updateClient(ClientDTO client) {
        log.info("enregistrement nouveau client");
        Client clientCreer=clientRepository.save(dtoMapper.fromClientDTO(client));
        return dtoMapper.fromClient(clientCreer);
    }
    @Override
    public void deleteClient(Long idClient){
        clientRepository.deleteById(idClient);
    }

   @Override
    public List<OperationDTO> getOperations(String idCompte){
        List<Operation> operations = operationRepository.findByCompteId(idCompte);
       return operations.stream().map(operation ->
            dtoMapper.fromOpreraton(operation)
        ).collect(Collectors.toList());
    }

    @Override
    public OperationsHistoryDTO getHistoryOperations(String idCompte, int page, int size) throws CompteNonTrouverException {
        Compte compte=compteRepository.findById(idCompte).orElse(null);
        if (compte==null) throw new CompteNonTrouverException("compte non trouver");
        Page<Operation> operations = operationRepository.findByCompteIdOrderByDateOperationDesc(idCompte, PageRequest.of(page, size));
        OperationsHistoryDTO operationsHistoryDTO= new OperationsHistoryDTO();
        List<OperationDTO> operationsHistoryDTOs = operations.getContent().stream().
                map(operation -> dtoMapper.
                        fromOpreraton(operation)).
                collect(Collectors.toList());
        operationsHistoryDTO.setOperationDTOList(operationsHistoryDTOs);
        operationsHistoryDTO.setId(compte.getId());
        operationsHistoryDTO.setSolde(compte.getSolde());
        operationsHistoryDTO.setCurrenPage(page);
        operationsHistoryDTO.setPageSize(size);
        operationsHistoryDTO.setTotalPages(operations.getTotalPages());
        return operationsHistoryDTO;
    }

    @Override
    public List<ClientDTO> searchClient(String keyword) {
        List<Client> clients=clientRepository.searchClient(keyword);
        List<ClientDTO> clientDTOS=clients.stream().map(cli->dtoMapper.fromClient(cli))
                .collect(Collectors.toList());
        return clientDTOS;
    }

    @Override
    public ClientDTO loadUserByEmail(String email) {
        Client client= clientRepository.findByEmail(email);
        return dtoMapper.fromClient(client);
    }
}
