package com.engsw.agenda;

import com.engsw.agenda.model.Agenda;

public class Mocks {
    static Agenda mockAgenda(String nome){
        return new Agenda(nome);
    }
}
