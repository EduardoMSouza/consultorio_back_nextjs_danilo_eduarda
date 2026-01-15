// FilaEsperaNotificacaoRequest.java
package consultorio.api.dto.request.agendamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilaEsperaNotificacaoRequest {

    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;

    @NotNull(message = "Tipo de notificação é obrigatório")
    private TipoNotificacao tipoNotificacao;

    @NotBlank(message = "Usuário é obrigatório")
    private String usuario;

    public enum TipoNotificacao {
        WHATSAPP,
        EMAIL,
        SMS,
        TELEFONE
    }
}