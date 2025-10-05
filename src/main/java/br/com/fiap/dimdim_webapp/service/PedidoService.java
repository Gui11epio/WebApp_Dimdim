package br.com.fiap.dimdim_webapp.service;

import br.com.fiap.dimdim_webapp.model.Cliente;
import br.com.fiap.dimdim_webapp.model.Pedido;
import br.com.fiap.dimdim_webapp.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    public PedidoService(PedidoRepository repo){ this.repo = repo; }

    public List<Pedido> findAll(){ return repo.findAll(); }
    public Optional<Pedido> findById(Long id){ return repo.findById(id); }
    public Pedido save(Pedido p){ return repo.save(p); }
    public void delete(Long id){ repo.deleteById(id); }
}
