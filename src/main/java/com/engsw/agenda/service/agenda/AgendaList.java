package com.engsw.agenda.service.agenda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class AgendaList implements IAgenda{
    
    private List<Contato> listaContato;

    //adicionar exceptions no add e remover

    @Override
    public Collection<Contato> criarLista() {
        this.listaContato = new ArrayList<>();
        return this.listaContato;
    }

    @Override
    public void adicionarContato(ContatoDTO ctt, Agenda ag) {
        if(this.listaContato != null){
            this.listaContato.add(ctt.transformaParaObj(ag));
        }
    }

    @Override
    public void removerContato(UUID cttId) {
        if(this.listaContato != null){
            this.listaContato.remove(cttId); //pt de atenção
        }
    }
}
