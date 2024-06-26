package com.example.ebankbackend;

import com.example.ebankbackend.dtos.ClientDTO;
import com.example.ebankbackend.dtos.CompteCourantDTO;
import com.example.ebankbackend.dtos.CompteDTO;
import com.example.ebankbackend.dtos.CompteEpargneDTO;
import com.example.ebankbackend.entities.*;
import com.example.ebankbackend.enums.StatusCompte;
import com.example.ebankbackend.enums.TypeOperation;
import com.example.ebankbackend.exceptions.ClientNonTrouve;
import com.example.ebankbackend.repositories.ClientRepository;
import com.example.ebankbackend.repositories.CompteRepository;
import com.example.ebankbackend.repositories.OperationRepository;
import com.example.ebankbackend.services.CompteService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankBackendApplication {
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(EBankBackendApplication.class, args);
    }


    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(CompteService compteService /*, JdbcUserDetailsManager jdbcUserDetailsManager*/ ){
        PasswordEncoder passwordEncoder=passwordEncoder();
        return args -> {
            Stream.of("malick","adama","mouhamed").forEach(name->{
                        ClientDTO client =new ClientDTO();
                        client.setName(name);
                        client.setEmail(name+"@gmail.com");
                        client.setPassword("kh9y7-dqfmf");
                compteService.creerClient(client);
               // compteService.addnewRole("ROLE_ADMIN");
                //compteService.addnewRole("ROLE_USER");
                try {
                    compteService.addRoleToClient(name+"@gmail.com","ADMIN");
                    compteService.addRoleToClient(name+"@gmail.com","USER");

                } catch (ClientNonTrouve e) {
                    throw new RuntimeException(e);
                }
                /*jdbcUserDetailsManager.createUser(
                        User.withUsername(client.getEmail()).password(passwordEncoder.encode("12345")).roles("USER","ADMIN").build()
                );*/
                    });
            
            compteService.listesClients().forEach(client -> {
                try {
                    compteService.creerCompteCourant(Math.random()*90000,9000, client.getId());
                    compteService.creerCompteEpargne(Math.random()*120000,5.5,client.getId());

                } catch (ClientNonTrouve e) {
                    e.printStackTrace();

                }

            });
            List<CompteDTO> comptes = compteService.listComptes();
            for(CompteDTO compte : comptes){
                for (int i = 0; i < 10; i++) {
                    String idCompte;
                    if (compte instanceof CompteEpargneDTO){
                        idCompte=((CompteEpargneDTO) compte).getId();
                    }else {
                        idCompte=((CompteCourantDTO)compte).getId();
                    }
                    compteService.depot(idCompte,10000+Math.random()*120000,"versement");
                    compteService.retrait(idCompte, 1000+Math.random()*9000,"retrait");

                }
            }
        };
    }
   // @Bean
    CommandLineRunner cml(JdbcUserDetailsManager jdbcUserDetailsManager){

        PasswordEncoder passwordEncoder = passwordEncoder();
        return args -> {
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user3").password(passwordEncoder.encode("12345")).roles("USER").build()

            );
            System.out.println(passwordEncoder.encode("12345"));
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user2").password(passwordEncoder.encode("12345")).roles("USER").build()
            );
            jdbcUserDetailsManager.createUser(
                    User.withUsername("admin1").password(passwordEncoder.encode("12345")).roles("USER","ADMIN").build()
            );


        };
    }
    //@Bean
    CommandLineRunner start(ClientRepository clientRepository,
                            CompteRepository compteRepository,
                            OperationRepository operationRepository,
                            JdbcUserDetailsManager jdbcUserDetailsManager){
        PasswordEncoder passwordEncoder=passwordEncoder();
        return  args -> {
            Stream.of("Assane","Yacine","Malick").forEach(name->{
               Client client=new Client();
               client.setName(name);
               client.setEmail(name+"@gmail.com");
               clientRepository.save(client);
            });
            clientRepository.findAll().forEach(client -> {
                CompteCourant cc=new CompteCourant();
                cc.setId(UUID.randomUUID().toString());
                cc.setClient(client);
                cc.setSolde(Math.random()*90000);
                cc.setDateCreation(new Date());
                cc.setDecouvert(9000);
                cc.setStatusCompte(StatusCompte.CREE);
                compteRepository.save(cc);
            });
            clientRepository.findAll().forEach(client -> {
                CompteEpargne ce=new CompteEpargne();
                ce.setId(UUID.randomUUID().toString());
                ce.setClient(client);
                ce.setSolde(Math.random()*90000);
                ce.setDateCreation(new Date());
                ce.setTauxInteret(5.5);
                ce.setStatusCompte(StatusCompte.CREE);
                compteRepository.save(ce);
            });
            compteRepository.findAll().forEach(compte -> {
                for (int i = 0; i < 10; i++) {
                    Operation op=new Operation();
                    op.setMontant(Math.random()*12000);
                    op.setDateOperation(new Date());
                    op.setTypeOperation(Math.random()>0.5?TypeOperation.CREDITER:TypeOperation.DEBITER);
                    op.setCompte(compte);
                    operationRepository.save(op);
                }
            });
        };

    }

}
