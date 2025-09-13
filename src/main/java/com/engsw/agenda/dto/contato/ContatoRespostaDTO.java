package com.engsw.agenda.dto.contato;

import java.time.LocalDateTime;
import java.util.UUID;

import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContatoRespostaDTO {
    private UUID id;
    private String agenda;
    private String nome;
    private String telefone;
    private LocalDateTime criadoEm;
    private LocalDateTime modificadoEm;

    public ContatoRespostaDTO(Contato c){
        this.id = c.getId();
        this.nome = c.getNome();
        this.agenda = c.getAgenda().getNome();
        this.telefone = c.getTelefone();
        this.criadoEm = c.getCriadoEm();
        this.modificadoEm = c.getModificadoEm();

    }
}
