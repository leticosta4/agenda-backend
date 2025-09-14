package com.engsw.agenda.service.agenda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.engsw.agenda.model.Contato;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AgendaList implements IAgenda{
    private List<Contato> listaContato = new ArrayList<>();

    @Override
    public Collection<Contato> getListaContato() {
        return this.listaContato;
    }
}
