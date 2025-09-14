package com.engsw.agenda.service;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.engsw.agenda.dto.contato.ContatoFiltroDTO;
import com.engsw.agenda.dto.contato.ContatoRespostaDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.model.Contato;
import com.engsw.agenda.repository.ContatoRepository;

@ExtendWith(MockitoExtension.class)
public class ContatoTest {
    //esses outros mocks n precisam do contexto do spring e o mockito que cuida
    @Mock private ContatoRepository contatoRepo;
    @InjectMocks private ContatoService contatoService;

    @Test
    public void deveBuscarContatos(){
        Agenda ag = new Agenda("teste");
        ag.setId(UUID.randomUUID());

        Contato ctt1 = new Contato("Letícia", "71999999999", ag);
        ctt1.setId(UUID.randomUUID());
        Contato ctt2 = new Contato("Alysson", "71988888888", ag);
        ctt2.setId(UUID.randomUUID());

        List<Contato> contatosBanco = List.of(ctt1, ctt2);

        Mockito.when(contatoRepo.findAll(any(Specification.class))).thenReturn(contatosBanco);
        ContatoFiltroDTO filtroVazio = new ContatoFiltroDTO("", "");
        List<ContatoRespostaDTO> resultado = contatoService.buscarContatos(filtroVazio);


        assertEquals(2, resultado.size());
        
        resultado.forEach(ctt -> {
            assertNotNull(ctt.getId());
            assertNotNull(ctt.getAgenda());
            assertNull(ctt.getModificadoEm());
            assertEquals("teste", ctt.getAgenda());
        });

        verify(contatoRepo, times(1)).findAll(any(Specification.class));

    }

    //testar ainda os filtros do specification
}
