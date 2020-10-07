package br.com.sevencomm.cobranca.domain.services;

import br.com.sevencomm.cobranca.data.repositories.CobrancaRepository;
import br.com.sevencomm.cobranca.data.repositories.ComentarioRepository;
import br.com.sevencomm.cobranca.data.repositories.UserRepository;
import br.com.sevencomm.cobranca.domain.interfaces.IComentarioService;
import br.com.sevencomm.cobranca.domain.models.Comentario;
import br.com.sevencomm.cobranca.domain.models.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService implements IComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UserRepository usuarioRepository;
    private final CobrancaRepository cobrancaRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, UserRepository usuarioRepository, CobrancaRepository cobrancaRepository) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    public void deleteComentario(Integer id) {
        Optional<Comentario> optComentario = comentarioRepository.findById(id);

        if (!optComentario.isPresent()) throw new IllegalArgumentException("Comentario not found");

        Comentario comentario = optComentario.get();
        Usuario usuario = getCurrentUser();

        if (!comentario.getUsuarioId().equals(usuario.getId()))
            throw new IllegalArgumentException("Somente o autor do comentario pode deletar");

        comentarioRepository.deleteById(id);
    }

    public Comentario getComentarioById(Integer id) {
        Optional<Comentario> optComentario = comentarioRepository.findById(id);

        if (!optComentario.isPresent()) throw new IllegalArgumentException("Comentario not found");

        return optComentario.get();
    }

    public Comentario insertComentario(Comentario comentario) {
        Usuario usuario = getCurrentUser();

        if (!cobrancaRepository.findById(comentario.getCobrancaId()).isPresent())
            throw new IllegalArgumentException("Cobranca not found");

        comentario.setUsuarioId(usuario.getId());
        comentario.setEdited(false);
        comentario.setDatahora(java.time.LocalDateTime.now().toString());

        return comentarioRepository.save(comentario);
    }

    public Comentario updateComentario(Comentario comentario, Integer id) {
        Usuario usuario = getCurrentUser();

        Optional<Comentario> optComentario = comentarioRepository.findById(id);

        if (!optComentario.isPresent()) throw new IllegalArgumentException("Comentario not found");

        Comentario saveComentario = optComentario.get();

        if (saveComentario.getUsuarioId() != usuario.getId())
            throw new IllegalArgumentException("User without edit permisions");

        saveComentario.setMessage(comentario.getMessage());
        saveComentario.setEdited(true);

        return comentarioRepository.save(saveComentario);
    }

    public List<Comentario> listComentariosByUsuarioId() {
        Usuario usuario = getCurrentUser();
        return comentarioRepository.findAllByUsuarioId(usuario.getId());
    }

    public List<Comentario> listComentariosByCobrancaId(Integer id) {
        return comentarioRepository.findAllByCobrancaId(id);
    }

    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Optional<Usuario> optUsuario = usuarioRepository.findById(usuario.getId());

        if (!optUsuario.isPresent())
            throw new IllegalArgumentException("Illegal api access");

        return optUsuario.get();
    }
}
