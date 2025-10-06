package br.com.fiap.dimdim_webapp.controller;

import br.com.fiap.dimdim_webapp.dto.PedidoRequest;
import br.com.fiap.dimdim_webapp.dto.PedidoResponse;
import br.com.fiap.dimdim_webapp.model.Pedido;
import br.com.fiap.dimdim_webapp.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service){
        this.service = service;
    }

    @GetMapping
    public List<PedidoResponse> all(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> get(@PathVariable Long id){
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PedidoResponse create(@RequestBody PedidoRequest pedidoRequest){
        return service.save(pedidoRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> update(@PathVariable Long id, @RequestBody PedidoRequest pedidoRequest){
        return service.update(id, pedidoRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}