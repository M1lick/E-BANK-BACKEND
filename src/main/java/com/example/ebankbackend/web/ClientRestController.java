package com.example.ebankbackend.web;

import com.example.ebankbackend.dtos.ClientDTO;
import com.example.ebankbackend.entities.Client;
import com.example.ebankbackend.exceptions.ClientNonTrouve;
import com.example.ebankbackend.services.CompteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")

public class ClientRestController {

    private CompteService compteService;

    @GetMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public List<ClientDTO> clients(){
        return compteService.listesClients();
    }
    @GetMapping("/clients/search")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")

    public List<ClientDTO> clients(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return compteService.searchClient("%"+keyword+"%");
    }
    @GetMapping("/clients/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public ClientDTO getClient(@PathVariable(name = "id") Long idClient) throws ClientNonTrouve {
        return compteService.getClient(idClient);
    }
    @PostMapping ("/clients")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ClientDTO creerClient(@RequestBody ClientDTO clientDTO) throws ClientNonTrouve {
        return compteService.creerClient(clientDTO);

    }
   @PutMapping("clients/{idClient}")
   @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ClientDTO updateClinet(@PathVariable Long idClient,@RequestBody ClientDTO clientDTO){
        clientDTO.setId(idClient);
        return compteService.updateClient(clientDTO);
    }
    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    void deleteClient(@PathVariable Long id){
        compteService.deleteClient(id);
    }

}
