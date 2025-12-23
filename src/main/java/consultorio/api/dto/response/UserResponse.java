package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String nome,
        String username,
        String email,
        User.Role role,
        Boolean ativo,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime criadoEm,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime ultimoLogin
) {}