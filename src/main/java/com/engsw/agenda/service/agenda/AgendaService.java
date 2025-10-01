package com.engsw.agenda.service.agenda;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engsw.agenda.dto.AgendaDTO;
import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;
import com.engsw.agenda.repository.AgendaRepository;
import com.engsw.agenda.repository.ContatoRepository;
import com.engsw.agenda.service.ContatoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

//so comunicação basica com os metodos crud de contato + encapsular oq vamos precisar para os 2 tipos de agenda

@Service
public class AgendaService {
    @Autowired private AgendaRepository agendaRepo;
    @Autowired private ContatoRepository contatoRepo;
    @Autowired private ContatoService contatoService;

    int TIPO_AGENDA = 1;
    FabricaAgenda fabrica = FabricaAgenda.getInstancia();
    IAgenda gerenciador = fabrica.criarListaAgenda(TIPO_AGENDA);


    @Transactional
    public Agenda criarAgenda(AgendaDTO dto){
        Agenda agenda = dto.transformaParaObj();
        Agenda agendaSalva = agendaRepo.save(agenda); 

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

        if(novoNome != null && !novoNome.isBlank()) {
            agenda.setNome(novoNome);
            agendaRepo.save(agenda);
        }
        return agenda;
    }


    @Transactional
    public ContatoRespostaDTO adicionarContatoAgenda(UUID idAgenda, ContatoDTO contatoDTO ){
        Contato contatoSalvo = contatoService.criarContato(contatoDTO, idAgenda);

        gerenciador.adicionarContato(contatoDTO, contatoSalvo.getAgenda());

        ContatoRespostaDTO cttCriado = new ContatoRespostaDTO(contatoSalvo);

        return cttCriado;
    }

    //revisar ainda
    @Transactional
    public void removerContatoAgenda(UUID idAgenda, UUID contatoId) {
        Agenda agenda = agendaRepo.findById(idAgenda)
                .orElseThrow(() -> new EntityNotFoundException("Agenda com ID " + idAgenda + " não encontrada."));

        contatoService.excluirContato(contatoId);
    }


    //revisar
    @Transactional
    public ContatoRespostaDTO editarContatoAgenda(UUID idAgenda, UUID contatoId, ContatoDTO contatoNovo) {
        Agenda agenda = agendaRepo.findById(idAgenda)
                .orElseThrow(() -> new EntityNotFoundException("Agenda com ID " + idAgenda + " não encontrada."));

        Contato contato = contatoRepo.findById(contatoId)
                .orElseThrow(() -> new EntityNotFoundException("Contato com ID " + contatoId + " não encontrado."));

        if (!contato.getAgenda().getId().equals(agenda.getId())) {
            throw new IllegalStateException("O contato " + contatoId + " não pertence à agenda " + idAgenda);
        }

        if (contatoNovo.getNome() != null) {
            contato.setNome(contatoNovo.getNome());
        }
        if (contatoNovo.getTelefone() != null) {
            contato.setTelefone(contatoNovo.getTelefone());
        }
        contato.setModificadoEm(LocalDateTime.now());

        Contato contatoSalvo = contatoRepo.save(contato);
        
        return new ContatoRespostaDTO(contatoSalvo);      
    }
}
