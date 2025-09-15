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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

import com.engsw.agenda.dto.contato.ContatoFiltroDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.service.ContatoService;

@WebMvcTest(controllers = ContatoController.class)
public class ContatoTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private ContatoService contatoService; //usa mockbean pq precisa do contexto do spring, e ele que cuida do mock

    public ContatoRespostaDTO preparaDTOs(String nome, String tel, String ag){
        ContatoRespostaDTO ctt = new ContatoRespostaDTO();
        ctt.setId(UUID.randomUUID());
        ctt.setNome(nome);
        ctt.setTelefone(tel);
        ctt.setAgenda(ag);
        ctt.setCriadoEm(LocalDateTime.now());

        return ctt;
    }
    
    @Test
    public void deveBuscarContatosPeloEndpoint() throws Exception{
        List<ContatoRespostaDTO> contatos = List.of(
            preparaDTOs("Letícia", "71999999999", "Agenda de Fulano"),
            preparaDTOs("Alysson", "71988888888", "Agenda de Fulano")
        );

        Mockito.when(contatoService.buscarContatos(ArgumentMatchers.any(ContatoFiltroDTO.class))).thenReturn(contatos); //mock do service

        mockMvc.perform(get("/contatos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[*].id").isNotEmpty())
                .andExpect(jsonPath("$[0].nome").value("Letícia"))
                .andExpect(jsonPath("$[1].nome").value("Alysson"))
                .andExpect(jsonPath("$[*].agenda", everyItem(is("Agenda de Fulano"))));

    }

    //criar, editar, apagar, get unico e get todos mas com filtro
}
