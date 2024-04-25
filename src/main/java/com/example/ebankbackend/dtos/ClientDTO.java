package com.example.ebankbackend.dtos;

import com.example.ebankbackend.entities.ClientRole;
import lombok.Data;

import java.util.List;


@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<String> roleList;
    private List<CompteDTO> compteDTOList;

}
