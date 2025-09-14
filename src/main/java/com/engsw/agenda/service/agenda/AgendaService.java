package com.engsw.agenda.service.agenda;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engsw.agenda.dto.AgendaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.repository.AgendaRepository;

import jakarta.transaction.Transactional;

//so comunicação basica com os metodos crud de contato
//encapsular oq vamos precisar para os 2 tipos de agenda

@Service
public class AgendaService {
    @Autowired private AgendaRepository agendaRepo;

    @Transactional
    public Agenda criarAgenda(AgendaDTO dto, int tipoAgenda){
        Agenda agenda = dto.transformaParaObj();
        Agenda agendaSalva = agendaRepo.save(agenda); 

        FabricaAgenda fabrica = FabricaAgenda.getInstancia();
        IAgenda gerenciador = fabrica.criarListaAgenda(tipoAgenda);

        agenda.setContatos(gerenciador.getListaContato());

        return agenda;         
    }

    public Optional<Agenda> retornaAgendaUnica(UUID idAgenda){ //rever o tipo do retorno
        return agendaRepo.findById(idAgenda);
    }
}
