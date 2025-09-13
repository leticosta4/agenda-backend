package com.engsw.agenda.service.agenda;
import java.util.HashMap;
import java.util.Collection;

import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AgendaMap{
    private HashMap<String, Contato> listaContato = new HashMap<String, Contato>();

    //talvez voltar para o modo inicial com herança da agenda no model
}
