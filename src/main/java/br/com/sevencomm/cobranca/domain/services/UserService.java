package br.com.sevencomm.cobranca.domain.services;

import br.com.sevencomm.cobranca.application.configs.exception.ObjectNotFoundException;
import br.com.sevencomm.cobranca.data.repositories.AreaRepository;
import br.com.sevencomm.cobranca.data.repositories.UserRepository;
import br.com.sevencomm.cobranca.data.repositories.UsuarioAreasRepository;
import br.com.sevencomm.cobranca.domain.interfaces.IUserService;
import br.com.sevencomm.cobranca.domain.models.Area;
import br.com.sevencomm.cobranca.domain.models.Usuario;
import br.com.sevencomm.cobranca.domain.models.UsuarioAreas;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final UsuarioAreasRepository userAreasRepository;

    public UserService(UserRepository userRepository, AreaRepository areaRepository, UsuarioAreasRepository userAreasRepository) {
        this.userRepository = userRepository;
        this.areaRepository = areaRepository;
        this.userAreasRepository = userAreasRepository;
    }

    public List<Usuario> listUsers() {
        return userRepository.findAll();
    }

    public List<Usuario> listUsersByAreaId(Integer id) {
        //return userRepository.findAllByAreaId(id);
        return null;
    }

    public Usuario getUser(Integer id) {
        Optional<Usuario> optUsuario = userRepository.findById(id);

        if (!optUsuario.isPresent()) throw new ObjectNotFoundException("User not found");

        return optUsuario.get();
    }

    public Usuario insertUser(Usuario usuario) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setSenha(encoder.encode(usuario.getSenha()));

        if (usuario.getEmail().equals("") || usuario.getEmail().length() < 10 || !usuario.getEmail().contains("@"))
            throw new IllegalArgumentException("Email inválido");

        if (usuario.getSenha().length() <= 6 || usuario.getSenha().length() != usuario.getSenha().replace(" ", "").length())
            throw new IllegalArgumentException("Senha inválida");

        if (usuario.getAreas() == null || !usuario.getAreas().isEmpty()) {
            assert usuario.getAreas() != null;
            for (Area area : usuario.getAreas()) {
                if (!areaRepository.findById(area.getId()).isPresent())
                    throw new IllegalArgumentException("Area " + area.getId() + " not found");
            }

            List<Area> areas = usuario.getAreas();
            usuario = userRepository.save(usuario);

            for (Area area : areas) {
                UsuarioAreas usuarioAreas = new UsuarioAreas();
                usuarioAreas.setAreaId(area.getId());
                usuarioAreas.setUsuarioId(usuario.getId());

                userAreasRepository.save(usuarioAreas);
            }

            List<Area> areaCompleta = new ArrayList<>();

            for (Area area : areas) {
                areaCompleta.add(areaRepository.findById(area.getId()).get());
            }

            usuario.setAreas(areaCompleta);

            return usuario;
        }

        return userRepository.save(usuario);
    }

    public void deleteUser(Integer id) {
        Usuario usuario = getCurrentUser();

        if (!usuario.getId().equals(id)) throw new IllegalArgumentException("Apenas o dono pode deletar a conta");

        userRepository.deleteById(id);
    }

    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Optional<Usuario> optUsuario = userRepository.findById(usuario.getId());

        if (!optUsuario.isPresent()) throw new IllegalArgumentException("Illegal api access");

        return optUsuario.get();
    }
}
