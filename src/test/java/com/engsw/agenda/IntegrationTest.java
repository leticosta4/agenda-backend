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

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
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

    @Test
    void testeEntrarAgenda() throws Exception {
        // cria agenda com nome conhecido
        String nome = "agenda-entrar-test";
        String agendaId = createAgenda(nome);

        // chama /agenda/entrar?nomeAgenda=nome
        mockMvc.perform(get("/agenda/entrar").param("nomeAgenda", nome))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(agendaId))
                .andExpect(jsonPath("$.nome").value(nome));
    }

    @Test
    void testeCriarContato_naAgenda() throws Exception {
        String agendaId = createAgenda("agenda para contato");

        Map<String, Object> contatoPayload = Map.of(
                "nome", "Contato Integra",
                "telefone", "99999999999"
        );
        String contatoId = createContact(agendaId, contatoPayload);

        // verificar via GET /agenda/{idAgenda}/contatos
        MvcResult getContatos = mockMvc.perform(get("/agenda/{idAgenda}/contatos", agendaId))
                .andExpect(status().isOk())
                .andReturn();

        String body = getContatos.getResponse().getContentAsString();
        List<Map<String,Object>> contatos = objectMapper.readValue(body, List.class);
        boolean contains = contatos.stream().anyMatch(c -> contatoId.equals(String.valueOf(c.get("id"))));
        assertTrue(contains, "Agenda deve conter o contato criado");
    }

    @Test
    void testeEditarContato_naAgenda_usandoGetPorId() throws Exception {
        String agendaId = createAgenda("agenda para editar contato");
        String contatoId = createContact(agendaId, Map.of(
                "nome", "Contato Para Editar",
                "telefone", "11111111111"
        ));

        // 1) GET por id do contato (endpoint: GET /agenda/{contatoId})
        MvcResult getContatoById = mockMvc.perform(get("/agenda/{contatoId}", contatoId))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String contatoBody = getContatoById.getResponse().getContentAsString();
        Map<String,Object> contatoObj = objectMapper.readValue(contatoBody, Map.class);

        // montar payload de atualização a partir do contato retornado (mantém campos obrigatórios)
        Map<String,Object> updateMap = new java.util.HashMap<>(contatoObj);
        updateMap.put("nome", "Contato Editado via GET");
        updateMap.put("telefone", "22222222222");
        // remover 'id' se o DTO de entrada não espera o campo id
        updateMap.remove("id");

        String updateJson = objectMapper.writeValueAsString(updateMap);

        // 2) PATCH /agenda/{idAgenda}/{contatoId}
        mockMvc.perform(patch("/agenda/{idAgenda}/{contatoId}", agendaId, contatoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contatoId))
                .andExpect(jsonPath("$.nome").value("Contato Editado via GET"))
                .andExpect(jsonPath("$.telefone").value("22222222222"));
    }

    @Test
    void testeDeletarContato_naAgenda() throws Exception {
        String agendaId = createAgenda("agenda para deletar contato");
        String contatoId = createContact(agendaId, Map.of(
                "nome", "Contato Para Deletar",
                "telefone", "33333333333"
        ));

        // DELETE /agenda/{idAgenda}/{contatoId}
        mockMvc.perform(delete("/agenda/{idAgenda}/{contatoId}", agendaId, contatoId))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        // confirmar remoção via GET /agenda/{idAgenda}/contatos
        MvcResult getAfter = mockMvc.perform(get("/agenda/{idAgenda}/contatos", agendaId))
                .andExpect(status().isOk())
                .andReturn();

        String afterBody = getAfter.getResponse().getContentAsString();
        List<Map<String,Object>> contatosAfter = objectMapper.readValue(afterBody, List.class);
        boolean stillExists = contatosAfter.stream().anyMatch(c -> contatoId.equals(String.valueOf(c.get("id"))));
        assertTrue(!stillExists, "Contato deve ter sido removido da agenda");
    }

    // helpers
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

        // cria contato (POST)
        mockMvc.perform(post("/agenda/{idAgenda}", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contatoJson))
                .andExpect(status().isCreated())
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print());

        // polling: busca contatos e aguarda até encontrar o contato criado com id não-nulo
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

            // se existir qualquer contato com id == null, aguarda e tenta novamente
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