package com.engsw.agenda.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.engsw.agenda.model.Contato;

public interface ContatoRepository extends JpaRepository<Contato, UUID>, JpaSpecificationExecutor<Contato>{
    List<Contato> findAll();
    Optional<Contato> findById(UUID idContato);
    boolean existsById(UUID idContato);
    void deleteById(UUID idContato);

}
