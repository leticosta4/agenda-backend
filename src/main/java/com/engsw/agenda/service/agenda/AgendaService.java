package com.engsw.agenda.service.agenda;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engsw.agenda.dto.AgendaDTO;
import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;
import com.engsw.agenda.repository.AgendaRepository;
import com.engsw.agenda.repository.ContatoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

//so comunicação basica com os metodos crud de contato + encapsular oq vamos precisar para os 2 tipos de agenda

// CRIAR A LISTA E MAP DE FORMA GLOBAL

@Service
public class AgendaService {
    @Autowired private AgendaRepository agendaRepo;
    @Autowired private ContatoRepository contatoRepo;

    @Transactional
    public Agenda criarAgenda(AgendaDTO dto, int tipoAgenda){
        Agenda agenda = dto.transformaParaObj();
        Agenda agendaSalva = agendaRepo.save(agenda); 

        FabricaAgenda fabrica = FabricaAgenda.getInstancia();
        IAgenda gerenciador = fabrica.criarListaAgenda(tipoAgenda);

        agendaSalva.setContatos(gerenciador.criarLista());

        return agendaSalva;         
    }

    public Optional<Agenda> retornaAgendaUnicaById(UUID idAgenda){ //rever o tipo do retorno
        return agendaRepo.findById(idAgenda);
    }

    public Agenda retornaAgendaUnicaByName(String nomeAgenda){ //rever o tipo do retorno
        return agendaRepo.findByNome(nomeAgenda);
    }

    public Agenda editarAgenda(UUID idAgenda, String novoNome){
        Agenda agenda = agendaRepo.findById(idAgenda)
            .orElseThrow(() -> new EntityNotFoundException("Agenda com ID " + idAgenda + " não encontrada"));

        if (novoNome != null && !novoNome.isBlank()) {
            agenda.setNome(novoNome);
            agendaRepo.save(agenda);
        }
        return agenda;
    }

//CONSERTAR O ADD E O REMOVER

    @Transactional
    public Contato adicionarContatoAgenda(UUID idAgenda, ContatoDTO contatoDTO ){
        Agenda agenda = agendaRepo.findById(idAgenda)
            .orElseThrow(() -> new EntityNotFoundException("Agenda com ID " + idAgenda + " não encontrada"));

        Contato novoContato = contatoDTO.transformaParaObj(agenda);
        Contato contatoSalvo = contatoRepo.save(novoContato);

        if (agenda.getContatos() != null) {
             agenda.getContatos().add(contatoSalvo);
        }

        return contatoSalvo;
    }

    @Transactional
    public void removerContatoAgenda(UUID idAgenda, UUID idContato){
        Agenda agenda = agendaRepo.findById(idAgenda)
            .orElseThrow(() -> new EntityNotFoundException("Agenda com ID " + idAgenda + " não encontrada"));

        if (!contatoRepo.existsById(idContato)) {
            throw new EntityNotFoundException("Contato com ID " + idContato + " não encontrado");
        }

        if (agenda.getContatos() != null) {
            // Isso usará a lógica corrigida (removeIf para List ou remove(key) para Map)
            agenda.getContatos().removeIf(contato -> contato.getId().equals(idContato));
            contatoRepo.deleteById(idContato);
        }

    }

    
}
