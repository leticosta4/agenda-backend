package com.engsw.agenda.service.agenda;

//falta refinar ainda
public class FabricaAgenda {
    public static final int AGENDAMAP = 0;
    public static final int AGENDALIST = 1;

    private static final FabricaAgenda fabricaAgenda = new FabricaAgenda();

    public static FabricaAgenda getInstancia(){
        return null;
    }

    private FabricaAgenda(){}

   

}

//melhorar esse singleton com factory