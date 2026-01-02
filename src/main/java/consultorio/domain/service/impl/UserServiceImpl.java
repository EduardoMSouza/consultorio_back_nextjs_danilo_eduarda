package consultorio.domain.service.impl;

import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.UserMapper;
import consultorio.domain.entity.User;
import consultorio.domain.entity.User.Role;
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

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    // ==================== CRUD ====================

    @Override
    @Transactional
    public UserResponse criar(UserRequest request, String criadoPor) {
        log.info("Criando usuário: {}", request.username());

        validarDuplicidade(request.username(), request.email(), null);

        User user = mapper.toEntity(request);
        user.setCriadoPor(criadoPor);
        user = repository.save(user);

        log.info("Usuário criado: id={}, username={}", user.getId(), user.getUsername());
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse buscarPorId(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    public UserResponse buscarPorUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + username));
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse buscarPorEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse atualizar(Long id, UserRequest request) {
        log.info("Atualizando usuário: {}", id);

        User user = findById(id);
        validarDuplicidade(request.username(), request.email(), id);

        mapper.updateEntityFromRequest(request, user);
        user = repository.save(user);

        log.info("Usuário atualizado: id={}", id);
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        User user = findById(id);
        validarUltimoAdmin(user);

        repository.deleteById(id);
        log.info("Usuário deletado: id={}", id);
    }

    // ==================== LISTAGENS ====================

    @Override
    public Page<UserResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<UserResponse> listarPorStatus(Boolean ativo, Pageable pageable) {
        return repository.findByAtivo(ativo, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<UserResponse> listarPorRole(Role role, Pageable pageable) {
        return repository.findByRole(role, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<UserResponse> buscar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listarTodos(pageable);
        }
        return repository.buscarPorTermo(termo.trim(), pageable).map(mapper::toResponse);
    }

    // ==================== STATUS ====================

    @Override
    @Transactional
    public void ativar(Long id) {
        repository.updateStatus(id, true, LocalDateTime.now());
        log.info("Usuário ativado: id={}", id);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        User user = findById(id);
        validarUltimoAdmin(user);

        repository.updateStatus(id, false, LocalDateTime.now());
        log.info("Usuário inativado: id={}", id);
    }

    // ==================== ADMIN ====================

    @Override
    @Transactional
    public void criarAdminInicial() {
        if (repository.existsByUsername(adminUsername)) {
            log.debug("Admin inicial já existe");
            return;
        }

        log.info("Criando usuário administrador inicial");

        User admin = User.builder()
                .nome("Administrador")
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .ativo(true)
                .criadoPor("SYSTEM")
                .build();

        repository.save(admin);
        log.info("Admin inicial criado - Username: {}, Email: {}", adminUsername, adminEmail);
    }

    // ==================== ESTATÍSTICAS ====================

    @Override
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("total", repository.count());
        stats.put("ativos", repository.countAtivos());

        stats.put("porRole", repository.countPorRole().stream()
                .collect(Collectors.toMap(r -> ((Role) r[0]).name(), r -> r[1])));

        return stats;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    private void validarDuplicidade(String username, String email, Long idExcluir) {
        boolean usernameDuplicado = idExcluir == null
                ? repository.existsByUsername(username)
                : repository.existsByUsernameExcludingId(username, idExcluir);

        if (usernameDuplicado) {
            throw new BusinessException("Username já existe");
        }

        boolean emailDuplicado = idExcluir == null
                ? repository.existsByEmail(email)
                : repository.existsByEmailExcludingId(email, idExcluir);

        if (emailDuplicado) {
            throw new BusinessException("Email já cadastrado");
        }
    }

    private void validarUltimoAdmin(User user) {
        if (user.getRole() == Role.ADMIN) {
            long adminCount = repository.countAtivosByRole(Role.ADMIN);
            if (adminCount <= 1) {
                throw new BusinessException("Não é possível remover/inativar o último administrador");
            }
        }
    }
}