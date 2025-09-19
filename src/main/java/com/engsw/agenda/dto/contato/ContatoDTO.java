package com.engsw.agenda.dto.contato;

import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContatoDTO {
    private String nome;
    private String telefone;

    public Contato transformaParaObj(Agenda agenda){
        return new Contato(nome, telefone, agenda);
    }
}
