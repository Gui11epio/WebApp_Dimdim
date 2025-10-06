package br.com.fiap.dimdim_webapp.dto;

import br.com.fiap.dimdim_webapp.model.Cliente;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class PedidoResponse {
    private Long id;
    private String descricao;
    private Double custo;
    private LocalDateTime dataCriacao;
    private ClienteResponse cliente;

    public PedidoResponse() {
    }

    public PedidoResponse(Long id, String descricao, Double custo, LocalDateTime dataCriacao, ClienteResponse cliente) {
        this.id = id;
        this.descricao = descricao;
        this.custo = custo;
        this.dataCriacao = dataCriacao;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponse cliente) {
        this.cliente = cliente;
    }
}
