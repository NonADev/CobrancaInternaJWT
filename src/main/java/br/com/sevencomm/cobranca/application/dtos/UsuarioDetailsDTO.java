package br.com.sevencomm.cobranca.application.dtos;

import br.com.sevencomm.cobranca.domain.models.Usuario;
import lombok.Data;

@Data
public class UsuarioDetailsDTO {
    private Integer id;
    private String login;
    private String nome;
    private String email;

    public static UsuarioDetailsDTO create(Usuario usuario) {
        UsuarioDetailsDTO dto = new UsuarioDetailsDTO();

        dto.setId(usuario.getId());
        dto.setLogin(usuario.getLogin());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());

        return dto;
    }
}
