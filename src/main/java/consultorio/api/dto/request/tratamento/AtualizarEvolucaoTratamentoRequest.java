package consultorio.api.dto.request.tratamento;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarEvolucaoTratamentoRequest {

    @PastOrPresent(message = "Data não pode ser futura")
    private LocalDate data;

    @Size(max = 2000, message = "Evolução e intercorrências devem ter no máximo 2000 caracteres")
    private String evolucaoEIntercorrencias;
}