package br.com.sevencomm.cobranca.data.repositories;

import br.com.sevencomm.cobranca.domain.models.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CobrancaRepository extends JpaRepository<Cobranca, Integer> {
    List<Cobranca> findAllByPagadorAreaId(Integer id);
    List<Cobranca> findAllByBeneficiarioAreaId(Integer id);
    List<Cobranca> findAllByPagadorAreaIdOrBeneficiarioAreaId(Integer pagadorAreaId, Integer beneficiarioAreaId);
    List<Cobranca> findAllByStatusId(Integer id);
}
