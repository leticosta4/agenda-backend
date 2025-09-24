package com.engsw.agenda.specification;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import com.engsw.agenda.model.Contato;

public class ContatoSpecification {
     public static Specification<Contato> filtrarPorNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }
  
  
    public static Specification<Contato> filtrarPorTelefone(String telefone) {
        return (root, query, cb) -> {
            if (telefone == null || telefone.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("telefone")), "%" + telefone.toLowerCase() + "%"); //Verificar se é telefone ou nome no attribute
        };
    }


    public static Specification<Contato> filtrarPorAgendaId(UUID agendaId) {
        return (root, query, cb) -> {
            if (agendaId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("agenda").get("id"), agendaId);
        };
    }
}
