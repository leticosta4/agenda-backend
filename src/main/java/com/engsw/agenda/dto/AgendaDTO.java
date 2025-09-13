package com.engsw.agenda.dto;

import com.engsw.agenda.model.Agenda;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgendaDTO {
    private String nome;

    public Agenda transformaParaObj(){
        return new Agenda(nome);
    }
}
