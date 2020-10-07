package br.com.sevencomm.cobranca.data.repositories;

import br.com.sevencomm.cobranca.domain.models.Area;
import br.com.sevencomm.cobranca.domain.models.UsuarioAreas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioAreasRepository extends JpaRepository<UsuarioAreas, Integer> {
    List<UsuarioAreas> findAllByUsuarioId(Integer id);
    List<UsuarioAreas> findAllByAreaId(Integer id);
}
