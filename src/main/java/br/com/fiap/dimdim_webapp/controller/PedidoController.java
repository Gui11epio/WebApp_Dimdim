package br.com.fiap.dimdim_webapp.controller;

import br.com.fiap.dimdim_webapp.model.Cliente;
import br.com.fiap.dimdim_webapp.model.Pedido;
import br.com.fiap.dimdim_webapp.service.ClienteService;
import br.com.fiap.dimdim_webapp.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service){ this.service = service; }

    @GetMapping
    public List<Pedido> all(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> get(@PathVariable Long id){
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pedido create(@RequestBody Pedido pedido){
        return service.save(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable Long id, @RequestBody Pedido pedido){
        return service.findById(id).map(existing -> {
            pedido.setId(existing.getId());
            return ResponseEntity.ok(service.save(pedido));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}