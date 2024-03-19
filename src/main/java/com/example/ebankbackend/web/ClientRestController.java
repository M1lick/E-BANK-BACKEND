package com.example.ebankbackend.web;

import com.example.ebankbackend.dtos.ClientDTO;
import com.example.ebankbackend.entities.Client;
import com.example.ebankbackend.exceptions.ClientNonTrouve;
import com.example.ebankbackend.services.CompteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")

public class ClientRestController {

    private CompteService compteService;

    @GetMapping("/clients")

    public List<ClientDTO> clients(){
        return compteService.listesClients();
    }
    @GetMapping("/clients/search")

    public List<ClientDTO> clients(@RequestParam(name = "keyword",defaultValue = "") String keyword){
        return compteService.searchClient("%"+keyword+"%");
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable(name = "id") Long idClient) throws ClientNonTrouve {
        return compteService.getClient(idClient);
    }
    @PostMapping ("/clients")
    public ClientDTO creerClient(@RequestBody ClientDTO clientDTO){
        return compteService.creerClient(clientDTO);

    }
   @PutMapping("clients/{idClient}")
    public ClientDTO updateClinet(@PathVariable Long idClient,@RequestBody ClientDTO clientDTO){
        clientDTO.setId(idClient);
        return compteService.updateClient(clientDTO);
    }
    @DeleteMapping("/clients/{id}")
    void deleteClient(@PathVariable Long id){
        compteService.deleteClient(id);
    }

}
