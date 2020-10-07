package br.com.sevencomm.cobranca.domain.interfaces;

import br.com.sevencomm.cobranca.domain.models.Comentario;

import java.util.List;

public interface IComentarioService {
    void deleteComentario(Integer id);

    Comentario getComentarioById(Integer id);

    Comentario insertComentario(Comentario comentario);
    Comentario updateComentario(Comentario comentario, Integer id);

    List<Comentario> listComentariosByUsuarioId();
    List<Comentario> listComentariosByCobrancaId(Integer id);
}
