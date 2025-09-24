package com.engsw.agenda.service.agenda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
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

    //talvez adicionar exceptions no add e remover

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
            this.listaContato.removeIf(contato -> contato.getId().equals(cttId)); //pt de atenção
        }
    }

    @Override
    public void editarContato(Contato ctt) {
        if(this.listaContato != null){
            this.listaContato.set(encontraIndiceContatoAntigo(ctt.getId()), ctt);
        }
    }


    public int encontraIndiceContatoAntigo(UUID id) {
        for (Contato c: this.listaContato) {
            if (c.getId().equals(id)) {
                return this.listaContato.indexOf(c);
            }
        }

        throw new NoSuchElementException("Contato com o ID " + id + " não encontrado na lista.");
    }
}
