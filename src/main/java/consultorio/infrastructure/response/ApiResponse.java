package consultorio.infrastructure.response;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Boolean sucesso;
    private String mensagem;
    private T dados;
    private Object erro;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Métodos factory
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .sucesso(true)
                .dados(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String mensagem, T data) {
        return ApiResponse.<T>builder()
                .sucesso(true)
                .mensagem(mensagem)
                .dados(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String mensagem) {
        return ApiResponse.<T>builder()
                .sucesso(false)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String mensagem, Object erro) {
        return ApiResponse.<T>builder()
                .sucesso(false)
                .mensagem(mensagem)
                .erro(erro)
                .timestamp(LocalDateTime.now())
                .build();
    }
}