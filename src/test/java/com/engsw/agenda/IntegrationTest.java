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

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void testeAddContatoAgenda() throws Exception {
        Map<String, Object> agendaPayload = Map.of("nome", "agenda para contato");
        String agendaJson = objectMapper.writeValueAsString(agendaPayload);

        MvcResult r = mockMvc.perform(post("/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(agendaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String agendaBody = r.getResponse().getContentAsString();
        Map<String, Object> agendaResp = objectMapper.readValue(agendaBody, Map.class);
        Object agendaId = agendaResp.get("id");
        assertThat(agendaId).isNotNull();

        Map<String, Object> contatoPayload = Map.of(
                "nome", "Contato Integra",
                "telefone", "99999999999"
        );
        String contatoJson = objectMapper.writeValueAsString(contatoPayload);

        mockMvc.perform(post("/agenda/{idAgenda}", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contatoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Contato Integra"));
    }
}
