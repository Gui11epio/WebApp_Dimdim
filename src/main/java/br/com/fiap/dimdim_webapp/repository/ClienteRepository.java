package br.com.fiap.dimdim_webapp.repository;

import br.com.fiap.dimdim_webapp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
