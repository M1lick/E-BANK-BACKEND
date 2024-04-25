package com.example.ebankbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor  @AllArgsConstructor @Builder
public class ClientRole {
    @Id
    private String role;

}
