package com.engsw.agenda.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

//usando static pq o acesso é a metodos n a classes
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.dto.contato.ContatoFiltroDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.service.ContatoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ContatoController.class)
public class ContatoTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private ContatoService contatoService; //usa mockbean pq precisa do contexto do spring, e ele que cuida do mock

    public ContatoRespostaDTO preparaDTOsResposta(String nome, String tel, String ag){
        ContatoRespostaDTO ctt = new ContatoRespostaDTO();
        ctt.setId(UUID.randomUUID());
        ctt.setNome(nome);
        ctt.setTelefone(tel);
        ctt.setAgenda(ag);
        ctt.setCriadoEm(LocalDateTime.now());

        return ctt;
    }

    public Agenda mockAgenda(){
        Agenda agenda = new Agenda("Agenda de Fulano");
        agenda.setId(UUID.randomUUID());
        return agenda;
    }

    public ContatoDTO mockContato(){
        ContatoDTO ctt = new ContatoDTO();
        ctt.setNome("contatoTeste");
        ctt.setTelefone("12345678900");

        return ctt;
    }

    
    @Test
    public void testeBuscarContatos() throws Exception{
        Agenda agenda = mockAgenda();
        List<ContatoRespostaDTO> contatos = List.of(
            preparaDTOsResposta("Letícia", "71999999999", agenda.getNome()),
            preparaDTOsResposta("Alysson", "71988888888", agenda.getNome())
        );

        Mockito.when(contatoService.buscarContatos(ArgumentMatchers.any(ContatoFiltroDTO.class))).thenReturn(contatos); //mock do service

        mockMvc.perform(get("/contatos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[0].nome").value(contatos.get(0).getNome()))
                .andExpect(jsonPath("$[1].nome").value(contatos.get(1).getNome()))
                .andExpect(jsonPath("$[*].agenda", everyItem(is(agenda.getNome()))));

    }

    @Test
    public void testeBuscarContatoUnico() throws Exception{
        Agenda agenda = mockAgenda();
        ContatoRespostaDTO cttTeste = preparaDTOsResposta("Cainan", "71977777777", "Agenda de Fulano");
        Mockito.when(contatoService.criarContato(Mockito.any(ContatoDTO.class), Mockito.eq(agenda.getId())))
           .thenReturn(cttTeste);

        mockMvc.perform(get("/contatos/{idContato}", cttTeste.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cttTeste.getId().toString()))
                .andExpect(jsonPath("$.nome").value(cttTeste.getNome()))
                .andExpect(jsonPath("$.agenda").value(agenda.getNome()));

    }

    @Test
    public void testeCriarContato() throws Exception {
        Agenda agenda = mockAgenda();
        ContatoDTO contatoDTO = mockContato();
        ContatoRespostaDTO cttTeste = preparaDTOsResposta("contatoTeste", "12345678900", agenda.getNome());

        Mockito.when(contatoService.criarContato(Mockito.any(ContatoDTO.class), Mockito.eq(agenda.getId()))).thenReturn(cttTeste);

        mockMvc.perform(post("/contatos/{agendaId}", agenda.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contatoDTO)))
                .andExpect(status().isCreated()) //201
                .andExpect(jsonPath("$.id").value(cttTeste.getId().toString()))
                .andExpect(jsonPath("$.nome").value(cttTeste.getNome()))
                .andExpect(jsonPath("$.agenda").value(agenda.getNome()));
    }

    @Test
    public void testeEditarContato() throws Exception{
        // ContatoRespostaDTO cttTeste = preparaDTOs("Cainan", "71977777777", "Agenda de Fulano");
        // Mockito.when(contatoService.buscarContatoPorId(cttTeste.getId())).thenReturn(Optional.of(cttTeste));

        // mockMvc.perform(get("/contatos/{idContato}", cttTeste.getId())
        //                 .contentType(MediaType.APPLICATION_JSON))
        //         .andExpect(status().isOk())
        //         .andExpect(jsonPath("$.id").value(cttTeste.getId().toString()))
        //         .andExpect(jsonPath("$.nome").value("Cainan"))
        //         .andExpect(jsonPath("$.agenda").value("Agenda de Fulano"));

    }
}

//editar, apagar, get unico e get todos mas com filtro
