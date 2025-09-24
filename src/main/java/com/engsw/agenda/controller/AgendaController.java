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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engsw.agenda.dto.AgendaDTO;
import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.dto.contato.ContatoFiltroDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.service.ContatoService;
import com.engsw.agenda.service.agenda.AgendaService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/agenda")
public class AgendaController {  //ESSE VAI SER O MAIN CONTROLLER
    @Autowired private AgendaService agendaService;
    @Autowired private ContatoService contatoService;

    @PostMapping("/") //essa barra poderia sair também
    public ResponseEntity<Agenda> criarAgenda(@RequestBody AgendaDTO agendaDTO) {
        Agenda nova = agendaService.criarAgenda(agendaDTO, 0); //tb estamos passando essa constante no service >> REVISAR
        return ResponseEntity.ok(nova);
    }


    @GetMapping("/entrar")
    public ResponseEntity<Agenda> entrarAgenda(@RequestParam String nomeAgenda) {
        Agenda agenda = agendaService.retornaAgendaUnicaByName(nomeAgenda);
        return ResponseEntity.ok(agenda);
    }


    @GetMapping("/{idAgenda}/contatos")
    public ResponseEntity<List<ContatoRespostaDTO>> buscarContatosAgenda(@PathVariable UUID idAgenda, @ParameterObject @ModelAttribute ContatoFiltroDTO filtro){
        List<ContatoRespostaDTO> contatos  = contatoService.buscarContatosPorAgenda(idAgenda, filtro.getNome(), filtro.getTelefone());
        return ResponseEntity.ok(contatos);
    }


    //não ta sendo usado no momento
    @PatchMapping("/{idAgenda}")
    public ResponseEntity<Agenda> editarAgenda(@PathVariable UUID idAgenda, @RequestParam String nomeAgendaNovo){
        Agenda novaAgenda = agendaService.editarAgenda(idAgenda, nomeAgendaNovo);

        return ResponseEntity.ok(novaAgenda);
    }

    
    @PostMapping("/{idAgenda}")
    public ResponseEntity<ContatoRespostaDTO> criarContato(@PathVariable UUID agendaId, @Valid @RequestBody ContatoDTO contatoNovo) {
        ContatoRespostaDTO cttCriado = agendaService.adicionarContatoAgenda(agendaId, contatoNovo); //manipula o gerenciador (list/map) + service de contato (banco)

        return ResponseEntity.status(HttpStatus.CREATED).body(cttCriado);
    }
    

    @DeleteMapping("/{idAgenda}/{contatoId}")
    public ResponseEntity<Void> excluirContato(@PathVariable UUID agendaId, @PathVariable UUID contatoId){
        agendaService.removerContatoAgenda(agendaId, contatoId);

        return ResponseEntity.noContent().build();
    }

    //controllers que faltam ser adaptados:
    //editar contato

}
