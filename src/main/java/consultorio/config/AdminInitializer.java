package consultorio.config;

import consultorio.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        try {
            userService.criarAdminInicial();
        } catch (Exception e) {
            log.error("Erro ao criar admin inicial", e);
        }
    }
}