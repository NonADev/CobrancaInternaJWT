package br.com.sevencomm.cobranca.domain.services;

import br.com.sevencomm.cobranca.application.configs.exception.ObjectNotFoundException;
import br.com.sevencomm.cobranca.data.repositories.AreaRepository;
import br.com.sevencomm.cobranca.data.repositories.UserRepository;
import br.com.sevencomm.cobranca.domain.interfaces.IAreaService;
import br.com.sevencomm.cobranca.domain.models.Area;
import br.com.sevencomm.cobranca.domain.models.Usuario;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaService implements IAreaService {

    private AreaRepository areaRepository;
    private UserRepository userRepository;

    public AreaService(AreaRepository areaRepository, UserRepository userRepository) {
        this.areaRepository = areaRepository;
        this.userRepository = userRepository;
    }

    public List<Area> listAreas() {
        return areaRepository.findAll();
    }

    public List<Usuario> listUsersByArea(Integer id) {
        return userRepository.findAllByAreaId(id);
    }

    public List<Area> listAreasByName(String nome) {
        if (nome.length() <= 1) throw new IllegalArgumentException("Minimum name size 2");

        return areaRepository.findByNomeLike("%" + nome + "%");
    }

    public Area getAreaById(Integer id) {
        Optional<Area> optArea = areaRepository.findById(id);

        if (!optArea.isPresent()) throw new ObjectNotFoundException("Area not found");

        return optArea.get();
    }

    public Area insertArea(Area area) {
        Assert.isNull(area.getId(), "Não foi possivel inserir registro, id não pode ser inserido");

        if (area.getNome().equals("") || area.getNome().length() <= 1)
            throw new IllegalArgumentException("Minimum name size 2");

        return areaRepository.save(area);
    }

    public Area updateArea(Area area, Integer id) {
        Optional<Area> fndArea = areaRepository.findById(id);

        if (!fndArea.isPresent()) throw new ObjectNotFoundException("Area not found");

        if (area.getNome() == null || area.getNome().equals("")) throw new IllegalArgumentException("Nome invalido");

        if (area.getNome().length() <= 3) throw new IllegalArgumentException("Minimum name size 4");

        Area auxArea = fndArea.get();

        auxArea.setNome(area.getNome());

        return areaRepository.save(auxArea);
    }

    public void deleteArea(Integer id) {
        areaRepository.deleteById(id);
    }
}
