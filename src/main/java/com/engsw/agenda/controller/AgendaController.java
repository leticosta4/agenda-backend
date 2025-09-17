package com.engsw.agenda.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engsw.agenda.dto.AgendaDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.service.ContatoService;
import com.engsw.agenda.service.agenda.AgendaService;



@RestController
@RequestMapping("/agenda")
public class AgendaController {
    @Autowired private AgendaService agendaService;
    @Autowired private ContatoService contatoService;

    @PostMapping("/criar")
    public ResponseEntity<Agenda> criarAgenda(@RequestBody AgendaDTO agendaDTO) { //revisar se esse tipo mesmo de retorno ou outro
        //ver como passar o numero certo para criação de list ou map
        Agenda nova = agendaService.criarAgenda(agendaDTO, 0);
        return ResponseEntity.ok(nova);
    }


    @GetMapping("/{idAgenda}")
    public ResponseEntity<Agenda> buscarId(@PathVariable UUID idAgenda) {
        return agendaService.retornaAgendaUnica(idAgenda)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idAgenda}")
    public ResponseEntity<Agenda> editarAgenda(@PathVariable UUID idAgenda, @RequestParam String nomeAgendaNovo){
        Agenda novaAgenda = agendaService.editarAgenda(idAgenda, nomeAgendaNovo);

        return ResponseEntity.ok(novaAgenda);
    }


    @GetMapping("/{idAgenda}/contatos")
    public ResponseEntity<List<ContatoRespostaDTO>> buscarContatosAgenda(@PathVariable UUID idAgenda){
        List<ContatoRespostaDTO> contatos  = contatoService.buscarContatosPorAgenda(idAgenda);
        return ResponseEntity.ok(contatos);


    }
}
