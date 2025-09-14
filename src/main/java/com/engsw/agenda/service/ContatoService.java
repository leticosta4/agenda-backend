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
import com.engsw.agenda.model.Contato;
import com.engsw.agenda.repository.ContatoRepository;
import com.engsw.agenda.specification.ContatoSpecification;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ContatoService {
    @Autowired private ContatoRepository contatoRepository;

    public List<ContatoRespostaDTO> buscarContatos(ContatoFiltroDTO contatoFiltroDTO){
        Specification<Contato> spec = Specification
                                               .where(ContatoSpecification.filtrarPorNome(contatoFiltroDTO.getNome()))
                                               .and(ContatoSpecification.filtrarPorTelefone(contatoFiltroDTO.getTelefone()));
        List<Contato> contatos = contatoRepository.findAll(spec);

        return contatos.stream().map(ContatoRespostaDTO::new).collect(Collectors.toList());
    }


    public Optional<ContatoRespostaDTO> buscarContatoPorId(UUID contatoId){
        Optional<Contato> contato = contatoRepository.findById(contatoId);

        return contato.map(ContatoRespostaDTO::new);
    }

    @Transactional
    public ContatoRespostaDTO editarContato(UUID contatoId, ContatoDTO contatoNovo){
        Contato contato = 
                        contatoRepository.findById(contatoId)
                        .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado"));


        if (contatoNovo.getNome() != null) {
            contato.setNome(contatoNovo.getNome());
        }
        if (contatoNovo.getTelefone() != null) {
            contato.setTelefone(contatoNovo.getTelefone());
        }
        contato.setModificadoEm(LocalDateTime.now());
        contatoRepository.save(contato);
        return new ContatoRespostaDTO(contato);
    }

    @Transactional
    public void excluirContato(UUID contatoId){
        if(!contatoRepository.existsById(contatoId)){
            throw new EntityNotFoundException("Contato não Encontrado");
        };

        contatoRepository.deleteById(contatoId);
    }

    public List<ContatoRespostaDTO> buscarContatosPorAgenda(UUID idAgenda){
       List<Contato> contatos = contatoRepository.findByAgendaId(idAgenda);

       return contatos.stream().map(ContatoRespostaDTO::new).collect(Collectors.toList());
    }

}
