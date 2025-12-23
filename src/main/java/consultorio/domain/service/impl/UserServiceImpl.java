package consultorio.domain.service.impl;

import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.UserMapper;
import consultorio.domain.entity.User;
import consultorio.domain.repository.UserRepository;
import consultorio.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:Admin@2025}")
    private String adminPassword;

    @Value("${app.admin.email:admin@consultorio.com}")
    private String adminEmail;

    @Override
    @Transactional
    public UserResponse criar(UserRequest request, String criadoPor) {
        log.info("Criando usuário: {}", request.username());

        if (repository.existsByUsername(request.username())) {
            throw new BusinessException("Username já existe");
        }

        if (repository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = mapper.toEntity(request);
        user.setCriadoPor(criadoPor);
        user = repository.save(user);

        log.info("Usuário {} criado com sucesso", user.getId());
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse buscarPorId(Long id) {
        User user = findById(id);
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse buscarPorUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + username));
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> listarPorStatus(Boolean ativo, Pageable pageable) {
        return repository.findByAtivo(ativo, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> listarPorRole(User.Role role, Pageable pageable) {
        return repository.findByRole(role, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public UserResponse atualizar(Long id, UserRequest request) {
        log.info("Atualizando usuário {}", id);

        User user = findById(id);

        if (!user.getUsername().equals(request.username()) &&
                repository.existsByUsername(request.username())) {
            throw new BusinessException("Username já existe");
        }

        if (!user.getEmail().equals(request.email()) &&
                repository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        mapper.updateEntityFromRequest(request, user);
        user = repository.save(user);

        log.info("Usuário {} atualizado com sucesso", id);
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        log.info("Inativando usuário {}", id);

        User user = findById(id);

        if (user.getRole() == User.Role.ADMIN) {
            long adminCount = repository.findByRole(User.Role.ADMIN, Pageable.unpaged()).getTotalElements();
            if (adminCount <= 1) {
                throw new BusinessException("Não é possível inativar o último administrador");
            }
        }

        user.setAtivo(false);
        repository.save(user);

        log.info("Usuário {} inativado", id);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        log.info("Ativando usuário {}", id);

        User user = findById(id);
        user.setAtivo(true);
        repository.save(user);

        log.info("Usuário {} ativado", id);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando usuário {}", id);

        User user = findById(id);

        if (user.getRole() == User.Role.ADMIN) {
            long adminCount = repository.findByRole(User.Role.ADMIN, Pageable.unpaged()).getTotalElements();
            if (adminCount <= 1) {
                throw new BusinessException("Não é possível deletar o último administrador");
            }
        }

        repository.deleteById(id);
        log.info("Usuário {} deletado", id);
    }

    @Override
    @Transactional
    public void criarAdminInicial() {
        if (!repository.existsByUsername(adminUsername)) {
            log.info("Criando usuário administrador inicial");

            User admin = User.builder()
                    .nome("Administrador")
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(User.Role.ADMIN)
                    .ativo(true)
                    .criadoPor("SYSTEM")
                    .build();

            repository.save(admin);

            log.info("Administrador inicial criado - Username: {}", adminUsername);
        }
    }

    private User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }
}