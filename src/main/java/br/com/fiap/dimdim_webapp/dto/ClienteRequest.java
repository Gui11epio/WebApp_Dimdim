package br.com.fiap.dimdim_webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import jakarta.persistence.Column;


public class ClienteRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    public ClienteRequest() {
    }

    public ClienteRequest(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public void setNome(@NotBlank String nome) {
        this.nome = nome;
    }

    public @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank String email) {
        this.email = email;
    }
}
