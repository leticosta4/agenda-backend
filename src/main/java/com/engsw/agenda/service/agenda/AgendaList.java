package com.engsw.agenda.service.agenda;
import java.util.ArrayList;
import java.util.List;

import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AgendaList{
    private List<Contato> listaContato = new ArrayList<>();

    //talvez voltar para o modo inicial com herança da agenda no model
}
