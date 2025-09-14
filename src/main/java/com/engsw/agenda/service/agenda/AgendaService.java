package com.engsw.agenda.service.agenda;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engsw.agenda.dto.AgendaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.repository.AgendaRepository;

import jakarta.persistence.EntityNotFoundException;
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

        //inserção no banco feita, falta ver como fazer a inserção em tempo de execução da list/hash usando o factory e o singleton
        return agendaSalva; //talvez mudar depois o tipo de retorno
    }

    public Optional<Agenda> retornaAgendaUnica(UUID idAgenda){ //rever o tipo do retorno
        return agendaRepo.findById(idAgenda);
    }

    public Agenda editarAgenda(UUID idAgenda, String novoNome){
        Agenda agenda = 
                        agendaRepo.findById(idAgenda)
                        .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));


        if(!novoNome.equals("")){
            agenda.setNome(novoNome);
            agendaRepo.save(agenda);
        }


        return agenda;
    }
}
