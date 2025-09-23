package com.engsw.agenda.controller;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engsw.agenda.dto.contato.*;
import com.engsw.agenda.model.Contato;
import com.engsw.agenda.service.ContatoService;
import com.engsw.agenda.service.agenda.AgendaService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/contatos")
public class ContatoController {
    @Autowired private ContatoService contatoService;
    @Autowired private AgendaService agendaService;

    @GetMapping
    public ResponseEntity<List<ContatoRespostaDTO>> listarContatos(@ParameterObject @ModelAttribute ContatoFiltroDTO filtro){
        List<ContatoRespostaDTO> contatos = contatoService.buscarContatos(filtro);
        return ResponseEntity.ok(contatos);
    }

    @PostMapping("/{agendaId}")
    public ResponseEntity<ContatoRespostaDTO> criarContato(@PathVariable UUID agendaId, @Valid @RequestBody ContatoDTO contatoNovo) {
        ContatoRespostaDTO contatoCriado = contatoService.criarContato(contatoNovo, agendaId);
        
        //chama o metodo de criar de agenda service
        agendaService.adicionarContatoAgenda(agendaId, contatoNovo);

        return ResponseEntity.status(HttpStatus.CREATED).body(contatoCriado);
    }
    

    @GetMapping("/{contatoId}")
    public ResponseEntity<ContatoRespostaDTO> buscarPorId(@PathVariable UUID contatoId){
        return contatoService.buscarContatoPorId(contatoId)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{contatoId}")
    public ResponseEntity<ContatoRespostaDTO> editarContato(@PathVariable UUID contatoId, @RequestBody ContatoDTO contatoNovo){
        ContatoRespostaDTO contatoEditado = contatoService.editarContato(contatoId, contatoNovo);
        return ResponseEntity.ok(contatoEditado);
    }

    @DeleteMapping("/{contatoId}")
    public ResponseEntity<Void> excluirContato(@PathVariable UUID contatoId){
        contatoService.excluirContato(contatoId);
        
        //Ainda n funciona
        //agendaService.removerContatoAgenda(contatoId);

        return ResponseEntity.noContent().build();
    }


}
