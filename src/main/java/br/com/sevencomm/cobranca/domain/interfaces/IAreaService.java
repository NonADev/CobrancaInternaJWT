package br.com.sevencomm.cobranca.domain.interfaces;

import br.com.sevencomm.cobranca.domain.models.Area;
import br.com.sevencomm.cobranca.domain.models.Usuario;

import java.util.List;

public interface IAreaService {
    void deleteArea(Integer areaId);

    Area getAreaById(Integer areaId);
    Area insertArea(Area area);
    Area updateArea(Area area, Integer id);

    List<Area> listAreas();
    List<Usuario> listUsersByArea(Integer id);
    List<Area> listAreasByName(String areaName);
}
