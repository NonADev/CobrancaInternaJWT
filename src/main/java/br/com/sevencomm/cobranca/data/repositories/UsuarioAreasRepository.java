package br.com.sevencomm.cobranca.data.repositories;

import br.com.sevencomm.cobranca.domain.models.Area;
import br.com.sevencomm.cobranca.domain.models.UsuarioAreas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioAreasRepository extends JpaRepository<UsuarioAreas, Integer> {
    Optional<UsuarioAreas> findFirstByUsuarioIdAndAreaId(Integer usuarioId, Integer areaId);

    List<UsuarioAreas> findAllByUsuarioId(Integer id);
    List<UsuarioAreas> findAllByAreaId(Integer id);
}
