package br.com.sevencomm.cobranca.data.repositories;

import br.com.sevencomm.cobranca.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByLogin(String login);
}
