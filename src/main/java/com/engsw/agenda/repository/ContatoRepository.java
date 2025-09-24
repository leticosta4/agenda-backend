package com.engsw.agenda.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.engsw.agenda.model.Contato;

public interface ContatoRepository extends JpaRepository<Contato, UUID>, JpaSpecificationExecutor<Contato>{
    List<Contato> findAll();
    Optional<Contato> findById(UUID idContato);
    List<Contato> findByAgendaId(UUID agendaId);
    boolean existsById(UUID idContato);
    void deleteById(UUID idContato);

    @Modifying
    @Query("DELETE FROM Contato c WHERE c.agenda.id = :idAgenda AND c.nome LIKE CONCAT('%', :nome, '%')")
    void deleteManyByNome(@Param("idAgenda") String idAgenda, @Param("nome") String nome);
}
