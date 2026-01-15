package consultorio.api.dto.response.pessoa;

import consultorio.domain.entity.pessoa.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String nome;
    private String username;
    private String email;
    private User.Role role;
    private Boolean ativo;
    private LocalDateTime ultimoLogin;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String criadoPor;
}