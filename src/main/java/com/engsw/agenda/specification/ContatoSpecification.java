package com.engsw.agenda.specification;
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
            return cb.like(cb.lower(root.get("nome")), "%" + telefone.toLowerCase() + "%");
        };
    }
}
