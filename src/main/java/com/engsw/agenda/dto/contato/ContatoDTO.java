package com.engsw.agenda.dto.contato;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContatoDTO {
    private String nome;
    private String telefone;

    public ContatoDTO(String nome, String telefone){
        this.nome = nome;
        this.telefone = telefone;
    }
}
