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

   public IAgenda criarListaAgenda(int tipoAgenda) {
        switch (tipoAgenda) {
            case AGENDAMAP:
                return new AgendaMap();
            case AGENDALIST:
                return new AgendaList();
            default:
                throw new IllegalArgumentException("Tipo de agenda inválido: " + tipoAgenda);
        }
    }

}