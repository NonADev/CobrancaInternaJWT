package br.com.sevencomm.cobranca.data.repositories;

import br.com.sevencomm.cobranca.domain.models.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Integer> {
    Optional<Area> findById(Integer id);
    List<Area> findByNomeLike(String nome);
}
