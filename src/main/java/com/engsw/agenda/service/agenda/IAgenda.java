package com.engsw.agenda.service.agenda;

import java.util.UUID;
import java.util.Collection;

import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.model.Agenda;

public interface IAgenda {
    public void adicionarContato(ContatoDTO ctt, Agenda ag);
    public void removerContato(UUID cttId);
    //public Collection criarListaTempParaAgenda(String nome);
}
