package com.engsw.agenda.service;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
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

    public Contato preparaObj(String nome, String tel, Agenda ag){
        Contato ctt = new Contato(nome, tel, ag);
        ctt.setId(UUID.randomUUID());
        return ctt;
    }

    @Test
    public void deveBuscarContatos(){
        Agenda ag = new Agenda("teste");
        ag.setId(UUID.randomUUID());

        Contato ctt1 = preparaObj("Letícia", "71999999999", ag);
        Contato ctt2 = preparaObj("Alysson", "71988888888", ag);

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

    @Test
    public void deveBuscarContatoUnico(){
        Agenda ag = new Agenda("teste");
        ag.setId(UUID.randomUUID());

        Contato ctt = preparaObj("Letícia", "71999999999", ag);
        UUID idContato = ctt.getId();

        Mockito.when(contatoRepo.findById(idContato)).thenReturn(Optional.of(ctt));
        Optional<ContatoRespostaDTO> resultadoOptional = contatoService.buscarContatoPorId(idContato);

        assertTrue(resultadoOptional.isPresent());
        ContatoRespostaDTO resultado = resultadoOptional.get();

        assertNotNull(resultado.getId());
        assertEquals(idContato, resultado.getId());
        assertEquals("Letícia", resultado.getNome());
        assertEquals("teste", resultado.getAgenda());

        verify(contatoRepo, times(1)).findById(idContato);

    }

    //testar ainda os filtros do specification
}
