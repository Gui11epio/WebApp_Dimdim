package br.com.fiap.dimdim_webapp.service;

import br.com.fiap.dimdim_webapp.dto.ClienteRequest;
import br.com.fiap.dimdim_webapp.dto.ClienteResponse;
import br.com.fiap.dimdim_webapp.model.Cliente;
import br.com.fiap.dimdim_webapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo){
        this.repo = repo;
    }

    public List<ClienteResponse> findAll(){
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<ClienteResponse> findById(Long id){
        return repo.findById(id)
                .map(this::toResponse);
    }

    public ClienteResponse save(ClienteRequest request){
        Cliente cliente = toEntity(request);
        Cliente savedCliente = repo.save(cliente);
        return toResponse(savedCliente);
    }

    public Optional<ClienteResponse> update(Long id, ClienteRequest request){
        return repo.findById(id).map(existing -> {
            updateEntityFromRequest(existing, request);
            Cliente updatedCliente = repo.save(existing);
            return toResponse(updatedCliente);
        });
    }

    public void delete(Long id){
        repo.deleteById(id);
    }

    // Métodos auxiliares para conversão
    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId().longValue(),
                cliente.getNome(),
                cliente.getEmail()
        );
    }

    private Cliente toEntity(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        return cliente;
    }

    private void updateEntityFromRequest(Cliente cliente, ClienteRequest request) {
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
    }
}
