package com.engsw.agenda.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="contato")
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="telefone", nullable = false)
    private String telefone;

    @Column(name="criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name="modificado_em")
    private LocalDateTime modificadoEm;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    public Contato(String nome, String telefone, Agenda agenda){
        this.nome = nome;
        this.telefone = telefone;
        this.criadoEm = LocalDateTime.now();
        this.agenda = agenda;
    }

    @PrePersist
    protected void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        modificadoEm = LocalDateTime.now();
    }
}
