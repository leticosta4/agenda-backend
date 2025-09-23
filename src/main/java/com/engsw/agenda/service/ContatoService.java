package com.engsw.agenda.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.dto.contato.ContatoFiltroDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;
import com.engsw.agenda.repository.*;
import com.engsw.agenda.specification.ContatoSpecification;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ContatoService {
    @Autowired private ContatoRepository contatoRepo;
    @Autowired private AgendaRepository agendaRepo;

    public List<ContatoRespostaDTO> buscarContatos(ContatoFiltroDTO contatoFiltroDTO){
        Specification<Contato> spec = Specification
                                               .where(ContatoSpecification.filtrarPorNome(contatoFiltroDTO.getNome()))
                                               .and(ContatoSpecification.filtrarPorTelefone(contatoFiltroDTO.getTelefone()));
        List<Contato> contatos = contatoRepo.findAll(spec);

        return contatos.stream().map(ContatoRespostaDTO::new).collect(Collectors.toList());
    }


    public Optional<ContatoRespostaDTO> buscarContatoPorId(UUID contatoId){
        Optional<Contato> contato = contatoRepo.findById(contatoId);

        return contato.map(ContatoRespostaDTO::new);
    }

    @Transactional
    public Contato criarContato(ContatoDTO dto, UUID agendaId){ //revisar tipo de retorno
        Agenda agenda = agendaRepo.findById(agendaId).orElseThrow(() -> new EntityNotFoundException("Agenda não encontrada"));
        
        //Verificacao de digito de telefone
        if(dto.getTelefone() == null || !dto.getTelefone().matches("\\d+")) {
            throw new IllegalArgumentException("Telefone inválido. Deve conter apenas dígitos numéricos.");
        }
        if(dto.getTelefone().length() != 11 ){
            throw new IllegalArgumentException("Telefone inválido. Deve conter exatamente 11 dígitos.");
        }

        Contato novoSalvo = contatoRepo.save(dto.transformaParaObj(agenda));
        return novoSalvo;
    }
    
    @Transactional
    public ContatoRespostaDTO editarContato(UUID contatoId, ContatoDTO contatoNovo){
        Contato contato = 
                        contatoRepo.findById(contatoId)
                        .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));


        if (contatoNovo.getNome() != null) {
            contato.setNome(contatoNovo.getNome());
        }
        if (contatoNovo.getTelefone() != null) {
            contato.setTelefone(contatoNovo.getTelefone());
        }
        contato.setModificadoEm(LocalDateTime.now());
        contatoRepo.save(contato);
        return new ContatoRespostaDTO(contato);
    }

    @Transactional
    public void excluirContato(UUID contatoId){
        if(!contatoRepo.existsById(contatoId)){
            throw new EntityNotFoundException("Contato não Encontrado");
        };

        contatoRepo.deleteById(contatoId);
    }

    public List<ContatoRespostaDTO> buscarContatosPorAgenda(UUID idAgenda){
       List<Contato> contatos = contatoRepo.findByAgendaId(idAgenda);

       return contatos.stream().map(ContatoRespostaDTO::new).collect(Collectors.toList());
    }

}
