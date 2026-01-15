package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.PlanoDental.StatusPlano;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlanoDentalResponse(
        Long id,
        Long pacienteId,
        Long dentistaId,
        String dente,
        String procedimento,
        BigDecimal valor,
        StatusPlano status,
        String observacoes,
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime criadoEm,
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime atualizadoEm
) {}
