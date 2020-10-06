package br.com.sevencomm.cobranca.domain.interfaces;

import br.com.sevencomm.cobranca.domain.models.Usuario;

import java.util.List;

public interface IUserService {
    void deleteUser(Integer id);

    Usuario getCurrentUser();
    Usuario getUser(Integer id);
    Usuario insertUser(Usuario usuario);

    List<Usuario> listUsers();
    List<Usuario> listUsersByAreaId(Integer id);
}
