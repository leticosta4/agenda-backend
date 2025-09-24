package com.engsw.agenda;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/01_init.sql") // garante estado conhecido do DB antes dos testes (opcional)
class AgendaControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void agendaCrud_flow_shouldWork() throws Exception {
        // 1) criar
        String nome = "Agenda Integra Test";
        String payload = "{\"nome\":\"" + nome + "\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> postResp = restTemplate.postForEntity("/agenda", request, String.class);
        assertTrue(postResp.getStatusCode().is2xxSuccessful(), "POST /agenda deve retornar 2xx (created)");

        // 2) listar e verificar existência
        ResponseEntity<String> listResp = restTemplate.getForEntity("/agenda", String.class);
        assertEquals(HttpStatus.OK, listResp.getStatusCode(), "GET /agenda deve retornar 200");

        List<Map<String, Object>> listas = objectMapper.readValue(listResp.getBody(), new TypeReference<>() {});
        Optional<Map<String, Object>> achado = listas.stream()
                .filter(m -> nome.equals(m.get("nome")))
                .findFirst();

        assertTrue(achado.isPresent(), "A agenda criada deve aparecer na listagem");
        Object id = achado.get().get("id");
        assertNotNull(id, "O registro listado deve conter campo 'id'");

        // 3) buscar por id
        ResponseEntity<String> getById = restTemplate.getForEntity("/agenda/{id}", String.class, id);
        assertEquals(HttpStatus.OK, getById.getStatusCode(), "GET /agendas/{id} deve retornar 200");

        Map<String, Object> foundObj = objectMapper.readValue(getById.getBody(), new TypeReference<>() {});
        assertEquals(nome, foundObj.get("nome"));

        // 4) deletar
        ResponseEntity<Void> deleteResp = restTemplate.exchange("/agenda/{id}", HttpMethod.DELETE, null, Void.class, id);
        assertTrue(deleteResp.getStatusCode() == HttpStatus.NO_CONTENT || deleteResp.getStatusCode() == HttpStatus.OK,
                "DELETE deve retornar 204 ou 200");

        // 5) confirmar remoção
        ResponseEntity<String> listAfterDelete = restTemplate.getForEntity("/agenda", String.class);
        List<Map<String,Object>> after = objectMapper.readValue(listAfterDelete.getBody(), new TypeReference<>() {});
        boolean stillExists = after.stream().anyMatch(m -> id.equals(m.get("id")));
        assertFalse(stillExists, "Registro deve ser removido após DELETE");
    }
}