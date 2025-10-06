package br.com.fiap.dimdim_webapp.controller;

import br.com.fiap.dimdim_webapp.dto.ClienteRequest;
import br.com.fiap.dimdim_webapp.dto.ClienteResponse;
import br.com.fiap.dimdim_webapp.model.Cliente;
import br.com.fiap.dimdim_webapp.service.ClienteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service){
        this.service = service;
    }

    @GetMapping
    public List<ClienteResponse> all(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> get(@PathVariable Long id){
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClienteResponse create(@RequestBody ClienteRequest clienteRequest){
        return service.save(clienteRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> update(@PathVariable Long id, @RequestBody ClienteRequest clienteRequest){
        return service.update(id, clienteRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
