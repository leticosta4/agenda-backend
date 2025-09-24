package com.engsw.agenda.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engsw.agenda.dto.contato.*;
import com.engsw.agenda.service.ContatoService;


@RestController
@RequestMapping("/contatos") //DEPRECADO
public class ContatoController {
    @Autowired private ContatoService contatoService;
    
    //nao ta sendo usado 
    @GetMapping("/{contatoId}") 
    public ResponseEntity<ContatoRespostaDTO> buscarPorId(@PathVariable UUID contatoId){
        return contatoService.buscarContatoPorId(contatoId)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    //FALTA ADDAPTAR
    @PatchMapping("/{contatoId}")
    public ResponseEntity<ContatoRespostaDTO> editarContato(@PathVariable UUID contatoId, @RequestBody ContatoDTO contatoNovo){
        ContatoRespostaDTO contatoEditado = contatoService.editarContato(contatoId, contatoNovo);
        return ResponseEntity.ok(contatoEditado);
    }

}
