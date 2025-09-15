package com.engsw.agenda.service.agenda;

import java.util.UUID;
import java.util.Collection;

import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;

public interface IAgenda {
    //Collection<Contato> getListaContato();
    public Collection<Contato> criarLista();

    public void adicionarContato(ContatoDTO ctt, Agenda ag);
    public void removerContato(UUID cttId);
}
