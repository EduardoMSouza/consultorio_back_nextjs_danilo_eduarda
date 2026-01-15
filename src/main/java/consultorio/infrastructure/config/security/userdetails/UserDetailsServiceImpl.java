package consultorio.infrastructure.config.security.userdetails;

import consultorio.domain.repository.pessoa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        log.debug("Carregando usuário por identificador: {}", identifier);

        // Busca em uma única query (username OR email)
        return userRepository.findByUsernameOrEmail(identifier)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", identifier);
                    return new UsernameNotFoundException("Usuário não encontrado: " + identifier);
                });
    }
}