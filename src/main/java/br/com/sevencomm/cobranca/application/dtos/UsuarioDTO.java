package br.com.sevencomm.cobranca.application.dtos;

import br.com.sevencomm.cobranca.domain.models.Usuario;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {
    private String access_token;
    private UsuarioDetailsDTO userDetails;

    public static UsuarioDTO create(Usuario usuario, String token) {
        UsuarioDTO dto = new UsuarioDTO();

        dto.setAccess_token(token);
        dto.setUserDetails(UsuarioDetailsDTO.create(usuario));

        return dto;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);
    }
}
