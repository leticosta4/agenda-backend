package com.engsw.agenda.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.engsw.agenda.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, UUID>{
    boolean existsByIdAgenda(UUID idAgenda);
    List<Agenda> findAll();
    Optional<Agenda> findByIdAgenda(UUID idAgenda);
}