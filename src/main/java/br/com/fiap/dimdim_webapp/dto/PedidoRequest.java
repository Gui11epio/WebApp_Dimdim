package br.com.fiap.dimdim_webapp.dto;

import br.com.fiap.dimdim_webapp.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class PedidoRequest {
    @NotBlank
    private String descricao;

    @NotBlank
    private Double custo;

    @NotBlank
    private LocalDateTime dataCriacao;

    @NotBlank
    private Long clienteId;

    public PedidoRequest() {
    }

    public PedidoRequest(String descricao, Double custo, LocalDateTime dataCriacao, Long clienteId) {
        this.descricao = descricao;
        this.custo = custo;
        this.dataCriacao = dataCriacao;
        this.clienteId = clienteId;
    }

    public @NotBlank String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NotBlank String descricao) {
        this.descricao = descricao;
    }

    public @NotBlank Double getCusto() {
        return custo;
    }

    public void setCusto(@NotBlank Double custo) {
        this.custo = custo;
    }

    public @NotBlank LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(@NotBlank LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public @NotBlank Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(@NotBlank Long clienteId) {
        this.clienteId = clienteId;
    }
}
