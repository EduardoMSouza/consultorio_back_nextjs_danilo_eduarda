package consultorio.infrastructure.security.userdetails;

import consultorio.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Tenta buscar por username primeiro
        return userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier)) // Se não achar, tenta email
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + identifier));
    }
}
