package br.com.fiap.dimdim_webapp.service;

import br.com.fiap.dimdim_webapp.dto.ClienteResponse;
import br.com.fiap.dimdim_webapp.dto.PedidoRequest;
import br.com.fiap.dimdim_webapp.dto.PedidoResponse;
import br.com.fiap.dimdim_webapp.model.Pedido;
import br.com.fiap.dimdim_webapp.repository.ClienteRepository;
import br.com.fiap.dimdim_webapp.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository repo, ClienteRepository clienteRepository){
        this.repo = repo;
        this.clienteRepository = clienteRepository;
    }

    public List<PedidoResponse> findAll(){
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<PedidoResponse> findById(Long id){
        return repo.findById(id)
                .map(this::toResponse);
    }

    public PedidoResponse save(PedidoRequest request){
        Pedido pedido = toEntity(request);
        Pedido savedPedido = repo.save(pedido);
        return toResponse(savedPedido);
    }

    public Optional<PedidoResponse> update(Long id, PedidoRequest request){
        return repo.findById(id).map(existing -> {
            updateEntityFromRequest(existing, request);
            Pedido updatedPedido = repo.save(existing);
            return toResponse(updatedPedido);
        });
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

    // Métodos auxiliares para conversão COM OS DTOs CORRIGIDOS
    private PedidoResponse toResponse(Pedido pedido) {
        ClienteResponse clienteResponse = null;
        if (pedido.getCliente() != null) {
            clienteResponse = new ClienteResponse(
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome(),
                    pedido.getCliente().getEmail()
            );
        }

        return new PedidoResponse(
                pedido.getId(),
                pedido.getDescricao(),
                pedido.getCusto(),
                pedido.getDataCriacao(),
                clienteResponse  // ✅ Agora é ClienteResponse, não Cliente
        );
    }

    private Pedido toEntity(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setDescricao(request.getDescricao());
        pedido.setCusto(request.getCusto());
        pedido.setDataCriacao(request.getDataCriacao() != null ?
                request.getDataCriacao() : LocalDateTime.now());

        // ✅ Agora busca pelo clienteId do PedidoRequest
        if (request.getClienteId() != null) {
            clienteRepository.findById(request.getClienteId())
                    .ifPresent(pedido::setCliente);
        } else {
            throw new IllegalArgumentException("ClienteId é obrigatório");
        }

        return pedido;
    }

    private void updateEntityFromRequest(Pedido pedido, PedidoRequest request) {
        pedido.setDescricao(request.getDescricao());
        pedido.setCusto(request.getCusto());

        if (request.getDataCriacao() != null) {
            pedido.setDataCriacao(request.getDataCriacao());
        }

        // ✅ Atualiza cliente se um novo clienteId for fornecido
        if (request.getClienteId() != null) {
            clienteRepository.findById(request.getClienteId())
                    .ifPresent(pedido::setCliente);
        }
    }
}