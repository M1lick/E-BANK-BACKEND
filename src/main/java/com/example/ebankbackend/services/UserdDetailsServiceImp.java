package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.ClientDTO;
import com.example.ebankbackend.entities.Client;
import com.example.ebankbackend.entities.ClientRole;
import com.example.ebankbackend.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserdDetailsServiceImp implements UserDetailsService {
    private CompteServiceImpl compteService;
    private ClientRepository clientRepository;




    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Utilisez le service CompteService pour charger les détails de l'utilisateur
       //Client client = compteService.loadUserByEmail(email);
        Client client = clientRepository.findByEmail(email);
        //String[] array = client.getRole().stream().map(u -> u.getRole()).toArray(String[]::new);
        /*List<String> roles = client.getRole().stream()
                .map(ClientRole::getRole)
                .collect(Collectors.toList());*/
        List<String> roles = client.getRole().stream()
                .map(clientRole -> clientRole.getRole())
                .collect(Collectors.toList());
        String[] rolesArray = roles.toArray(new String[roles.size()]);
        System.out.println(roles);
        /*for (String role : rolesArray) {
            System.out.println(role);
        }
        String[] cheikhchiffre=rolesArray;
        String rs=String.join(" ROLE_",cheikhchiffre);



        //je recuper le resutat de la liste des roles transformer en String que je vais injecter dans mon UserDetail.roles()
        System.out.println(rs);  */
        StringBuilder rolesAvecRol = new StringBuilder();
       // rolesAvecRol.append("ROLE_").append(role).append(" ");
// Parcours du tableau des rôles
        for (int i = 0; i < rolesArray.length; i++) {
            if (i == 0) {
                rolesAvecRol.append(rolesArray[i]).append(" ");
                continue;

            }
            // Concaténation de "rol" au début de chaque rôle et ajout à la chaîne
            rolesAvecRol.append("ROLE_").append(rolesArray[i]).append(" ");

        }
        //rolesAvecRol.deleteCharAt(rolesAvecRol.length() - 1);
        System.out.println(rolesAvecRol.toString());



        if (client == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }

        // Créez un UserDetails à partir des détails de l'utilisateur
        // Ici, vous devez adapter cette partie en fonction de votre logique métier
        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())
                .roles(rolesAvecRol.toString())
                .build();
    }
}
