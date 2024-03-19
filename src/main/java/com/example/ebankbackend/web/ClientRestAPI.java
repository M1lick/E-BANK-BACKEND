package com.example.ebankbackend.web;

import com.example.ebankbackend.dtos.*;
import com.example.ebankbackend.exceptions.CompteNonTrouverException;
import com.example.ebankbackend.exceptions.SoldeInsuffisantException;
import com.example.ebankbackend.services.CompteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ClientRestAPI  {
    private CompteService compteService;

    public ClientRestAPI(CompteService compteService) {
        this.compteService = compteService;
    }

   @GetMapping("comptes/{idCompte}")
    public CompteDTO  getCompte(@PathVariable String idCompte) throws CompteNonTrouverException {
        return compteService.getCompte(idCompte);

    }
    @GetMapping("/comptes")
    public List<CompteDTO> listCopmtes(){
        return compteService.listComptes();

    }
    @GetMapping("/comptes/{idCompte}/operations")
    public  List<OperationDTO> getOperations(@PathVariable String idCompte){
        return compteService.getOperations(idCompte);
    }

    @GetMapping("/comptes/{idCompte}/pageOperations")
    public OperationsHistoryDTO getCompteOperations(@PathVariable String idCompte ,
                                                    @RequestParam(name="page",defaultValue = "0")int page,
                                                    @RequestParam(name="size",defaultValue = "5")int size) throws CompteNonTrouverException {

        return compteService.getHistoryOperations(idCompte,page,size);
    }
    @PostMapping("/comptes/retrait")
    public RetraitDTO retrait(@RequestBody RetraitDTO retraitDTO) throws SoldeInsuffisantException, CompteNonTrouverException {
        this.compteService.retrait(retraitDTO.getCompteId(), retraitDTO.getMontant(), retraitDTO.getDescription());
        return  retraitDTO;
    }
    @PostMapping("/comptes/depot")
    public DepotDTO depot(@RequestBody DepotDTO depotDTO) throws  CompteNonTrouverException {
        this.compteService.depot(depotDTO.getCompteId(), depotDTO.getMontant(), depotDTO.getDescription());
        return  depotDTO;
    }
    @PostMapping("/comptes/transfer")
    public void transfer(@RequestBody  TransferRequestDTO transferRequestDTO) throws SoldeInsuffisantException, CompteNonTrouverException {
        this.compteService.transfer(transferRequestDTO.getCompteSource(),
                transferRequestDTO.getCompteDestination(),
                transferRequestDTO.getMontant());
    }

}
