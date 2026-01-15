package consultorio.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EvolucaoTratamentoRequest(
        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "ID do dentista é obrigatório")
        Long dentistaId,

        @NotNull(message = "Data da evolução é obrigatória")
        LocalDate dataEvolucao,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Size(max = 2000)
        String procedimentosRealizados,

        @Size(max = 2000)
        String observacoes,

        LocalDate proximaConsulta
) {}
