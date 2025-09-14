package com.engsw.agenda.service.agenda;
import java.util.Collection;
import java.util.HashMap;

import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AgendaMap implements IAgenda{
    private HashMap<String, Contato> listaContato = new HashMap<String, Contato>();

    @Override
    public Collection<Contato> getListaContato() {
        // Para o mapa, retornamos apenas os valores, que formam uma Collection<Contato>.
        return this.listaContato.values();
    }
}
