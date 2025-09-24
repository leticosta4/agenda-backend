package com.engsw.agenda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.engsw.agenda.dto.contato.ContatoDTO;
import com.engsw.agenda.model.Agenda;
import com.engsw.agenda.repository.AgendaRepository;
import com.engsw.agenda.service.agenda.AgendaService;

//REFATORAR TUDO

@ExtendWith(MockitoExtension.class)
public class AgendaServiceTest {

    @Mock private AgendaRepository agendaRepo;

    @InjectMocks private AgendaService agendaService;

    @Test
    public void testeAdicionarContatoNaListaEmMemoria() {

        UUID agendaId = UUID.randomUUID();
        ContatoDTO contatoDTO1 = new ContatoDTO("Fulano de Tal", "71911111111");
        ContatoDTO contatoDTO2 = new ContatoDTO("Ciclana da Silva", "71922222222");

        Agenda agendaMock = new Agenda("Agenda de Teste");
        agendaMock.setId(agendaId);
        agendaMock.setContatos(new ArrayList<>()); 

        when(agendaRepo.findById(agendaId)).thenReturn(Optional.of(agendaMock));


        // ========== 2. AÇÃO (Act) ==========

        // Chamamos o método que queremos testar.
        // Este método deve encontrar a 'agendaMock' e adicionar um contato à sua lista.
        agendaService.adicionarContatoAgenda(agendaId, contatoDTO1);
        agendaService.adicionarContatoAgenda(agendaId, contatoDTO2);


        // ========== 3. VERIFICAÇÃO (Assert) ==========

        // Resultado Esperado 1: A lista de contatos da agenda não deve ser nula e não deve estar vazia.
        assertNotNull(agendaMock.getContatos());
        assertEquals(2, agendaMock.getContatos().size());
        

        // Resultado Esperado 3: Os dados do contato na lista devem ser os mesmos que passamos no DTO.
        // Usamos .iterator().next() para pegar o primeiro (e único) item da coleção.
        List<String> nomesNaLista = agendaMock.getContatos()
                                           .stream()
                                           .map(contato -> contato.getNome())
                                           .toList();
            assertTrue(nomesNaLista.contains("Fulano de Tal"));
            assertTrue(nomesNaLista.contains("Ciclana da Silva"));

        // Resultado Esperado 4 (Opcional, mas recomendado): Verificar se o repositório foi chamado.
        // Isso garante que o método realmente tentou buscar a agenda no "banco".
        verify(agendaRepo, times(2)).findById(agendaId);
    }
}