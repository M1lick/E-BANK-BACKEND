package com.example.ebankbackend.security;


import com.example.ebankbackend.entities.Client;
import com.example.ebankbackend.repositories.ClientRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder;
    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        return authentication ;
    }

@PostMapping("login")
    public Map<String,String> login(String username , String password){
       Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    Instant instant=Instant.now();
   String scope= authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ") );
    JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
            .issuedAt(instant)
            .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
            .subject(username)
            .claim("scope",scope)
            .build();
    JwtEncoderParameters jwtEncoderParameters=
            JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );
    //recupreration des info client dont j'ai beson
    Client client=clientRepository.findByEmail(username);
    String idCompte=clientRepository.findCompteCC(client.getId());
    Map<String, String> map = new HashMap<>();
    map.put("idcompt", idCompte);
    map.put("k2", client.getName());
    String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    map.put("access=token", jwt);


    return Map.of("access=token", jwt ,"idcompt",idCompte ,"name",client.getName());
    /*
    String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    return Map.of("access=token",jwt); */

  }

}
