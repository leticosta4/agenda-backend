package com.engsw.agenda.dto.contato;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContatoFiltroDTO {
   private String nome;
    private String telefone;

    public ContatoFiltroDTO(String nome, String telefone){
        this.nome = nome;
        this.telefone = telefone;
    }
}
