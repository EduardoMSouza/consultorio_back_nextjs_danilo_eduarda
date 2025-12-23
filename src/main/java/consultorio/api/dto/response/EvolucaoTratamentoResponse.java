package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EvolucaoTratamentoResponse(
        Long id,
        Long pacienteId,
        Long dentistaId,
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataEvolucao,
        
        String descricao,
        String procedimentosRealizados,
        String observacoes,
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate proximaConsulta,
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime criadoEm,
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime atualizadoEm
) {}
