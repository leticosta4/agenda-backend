package com.engsw.agenda.service.agenda;
import java.util.Collection;
import java.util.HashMap;
import java.util.Collection;
import java.util.UUID;

import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AgendaMap implements IAgenda{
    private HashMap<UUID, Contato> listaContato;

    //adicionar os exceptions no add e no remover

    @Override
    public Collection<Contato> criarLista() {
        // this.listaContato = new HashMap<String, Contato>();
        this.listaContato = new HashMap<UUID, Contato>();
        return this.listaContato.values();
    }

    @Override
    public void adicionarContato(ContatoDTO ctt, Agenda ag) {
        if(this.listaContato != null){
            this.listaContato.put(UUID.randomUUID(), ctt.transformaParaObj(ag));
        }
    }

    @Override
    public void removerContato(UUID cttId) {
        if(this.listaContato != null){
            this.listaContato.remove(UUID.randomUUID());
        }
    }
}
