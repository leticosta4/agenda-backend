package com.engsw.agenda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class IntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test //ok
    void testeCriarAgenda() throws Exception {
        Map<String, Object> payload = Map.of("nome", "agenda test");
        String json = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post("/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("agenda test"));
    }

    
    @Test //ok
    void testeEntrarAgenda() throws Exception {
        String nome = "agenda-entrar-test";
        String agendaId = createAgenda(nome);

        mockMvc.perform(get("/agenda/entrar").param("nomeAgenda", nome))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendaId))
                .andExpect(jsonPath("$.nome").value(nome));
    }

    @Test //ok
    void testeAddCriarContatoAgenda() throws Exception {
        String agendaId = createAgenda("agenda para contato");

        Map<String, Object> contatoPayload = Map.of(
                "nome", "Contato Integra",
                "telefone", "99999999999"
        );
        String contatoId = createContact(agendaId, contatoPayload);

        MvcResult getContatos = mockMvc.perform(get("/agenda/{idAgenda}/contatos", agendaId))
                .andExpect(status().isOk())
                .andReturn();

        String body = getContatos.getResponse().getContentAsString();
        List<Map<String,Object>> contatos = objectMapper.readValue(body, List.class);
        boolean contains = contatos.stream().anyMatch(c -> contatoId.equals(String.valueOf(c.get("id"))));
        assertTrue(contains, "Agenda deve conter o contato criado");
    }


    @Test
    void testeEditarContatoAgenda() throws Exception {
        String agendaId = createAgenda("agenda para editar contato");
        String contatoId = createContact(agendaId, Map.of(
                "nome", "Contato Para Editar",
                "telefone", "11111111111"
        ));

        Map<String, Object> updatePayload = Map.of(
                "nome", "Contato Editado",
                "telefone", "22222222222"
        );
        String updateJson = objectMapper.writeValueAsString(updatePayload);

        mockMvc.perform(patch("/agenda/{idAgenda}/{contatoId}", agendaId, contatoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contatoId))
                .andExpect(jsonPath("$.nome").value("Contato Editado"))
                .andExpect(jsonPath("$.telefone").value("22222222222"));
    }


    @Test
    void testeDeletarContato() throws Exception {
        // 1. Setup: Cria uma agenda e um contato
        String agendaId = createAgenda("agenda para deletar contato");
        String contatoId = createContact(agendaId, Map.of(
                "nome", "Contato Para Deletar",
                "telefone", "33333333333"
        ));

        // 2. Ação: Envia a requisição DELETE para remover o contato
        mockMvc.perform(delete("/agenda/{idAgenda}/{contatoId}", agendaId, contatoId))
                .andExpect(status().isNoContent());

        // 3. Verificação: Busca todos os contatos da agenda e confirma que o contato foi removido
        MvcResult result = mockMvc.perform(get("/agenda/{idAgenda}/contatos", agendaId))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Map<String, Object>> contatosRestantes = objectMapper.readValue(content, List.class);

        boolean aindaExiste = contatosRestantes.stream()
                .anyMatch(contato -> contato.get("id").equals(contatoId));

        assertTrue(!aindaExiste, "O contato deveria ter sido removido da agenda.");
   }

    @Test
    void testeDeletarVariosContatosPorNome() throws Exception {
        String agendaId = createAgenda("agenda para deletar varios contatos");
        String contatoId1 = createContact(agendaId, Map.of(
                "nome", "Contato Comum 1",
                "telefone", "11111111111"
        ));
        String contatoId2 = createContact(agendaId, Map.of(
                "nome", "Contato Comum 2",
                "telefone", "22222222222"
        ));
        String contatoId3 = createContact(agendaId, Map.of(
                "nome", "Outro Contato",
                "telefone", "33333333333"
        ));

        // DELETE /agenda/{idAgenda}/contatos?nome=Contato Comum
        mockMvc.perform(delete("/agenda/{idAgenda}/contatos/remover", agendaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\": \"Contato Comum\"}"))
                .andExpect(status().isNoContent());

        // confirmar remoção via GET /agenda/{idAgenda}/contatos
        MvcResult getAfter = mockMvc.perform(get("/agenda/{idAgenda}/contatos", agendaId))
                .andExpect(status().isOk())
                .andReturn();

        String afterBody = getAfter.getResponse().getContentAsString();
        List<Map<String,Object>> contatosAfter = objectMapper.readValue(afterBody, List.class);
        boolean stillExists1 = contatosAfter.stream().anyMatch(c -> contatoId1.equals(String.valueOf(c.get("id"))));
        boolean stillExists2 = contatosAfter.stream().anyMatch(c -> contatoId2.equals(String.valueOf(c.get("id"))));
        boolean stillExists3 = contatosAfter.stream().anyMatch(c -> contatoId3.equals(String.valueOf(c.get("id"))));
        assertTrue(!stillExists1, "Contato 1 deve ter sido removido da agenda");
        assertTrue(!stillExists2, "Contato 2 deve ter sido removido da agenda");
        assertTrue(stillExists3, "Outro Contato não deve ter sido removido da agenda");
    }

    
    // "mocks"
    private String createAgenda(String nome) throws Exception {
        Map<String, Object> agendaPayload = Map.of("nome", nome);
        String agendaJson = objectMapper.writeValueAsString(agendaPayload);

        MvcResult r = mockMvc.perform(post("/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(agendaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String agendaBody = r.getResponse().getContentAsString();
        Map<String, Object> agendaResp = objectMapper.readValue(agendaBody, Map.class);
        String agendaId = agendaResp.get("id").toString();
        UUID.fromString(agendaId);
        return agendaId;
    }

    private String createContact(String agendaId, Map<String, Object> contatoPayload) throws Exception {
        String contatoJson = objectMapper.writeValueAsString(contatoPayload);

        //cria ctt
        mockMvc.perform(post("/agenda/{idAgenda}", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contatoJson))
                .andExpect(status().isCreated())
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print());

        //busca ctts e aguarda ate encontrar o ctt criado com id n nulo
        String nome = String.valueOf(contatoPayload.get("nome"));
        String telefone = contatoPayload.get("telefone") != null ? String.valueOf(contatoPayload.get("telefone")) : null;

        final int maxAttempts = 20;
        final long delayMs = 200;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            MvcResult getContatos = mockMvc.perform(get("/agenda/{idAgenda}/contatos", agendaId))
                    .andExpect(status().isOk())
                    .andReturn();

            String body = getContatos.getResponse().getContentAsString();
            List<Map<String,Object>> contatos = objectMapper.readValue(body, List.class);

            //se tiver qualquer ctt com id == null, aguarda e tenta novamente
            boolean anyNullId = contatos.stream().anyMatch(c -> c.get("id") == null);
            if (anyNullId) {
                Thread.sleep(delayMs);
                continue;
            }

            for (Map<String,Object> c : contatos) {
                String cNome = c.get("nome") != null ? String.valueOf(c.get("nome")) : null;
                String cTel = c.get("telefone") != null ? String.valueOf(c.get("telefone")) : null;
                if (nome.equals(cNome) && (telefone == null || telefone.equals(cTel))) {
                    String contatoId = String.valueOf(c.get("id"));
                    UUID.fromString(contatoId);
                    return contatoId;
                }
            }

            Thread.sleep(delayMs);
        }

        throw new IllegalStateException("Não foi possível encontrar o contato criado com id não-nulo na agenda após polling");
    }
}