package consultorio.domain.service.user.impl;

import consultorio.api.dto.request.pessoa.UserRequest;
import consultorio.api.dto.response.pessoa.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.pessoa.UserMapper;
import consultorio.domain.entity.pessoa.User;
import consultorio.domain.entity.pessoa.User.Role;
import consultorio.domain.repository.pessoa.UserRepository;
import consultorio.domain.service.user.UserService;
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

    @Override
    @Transactional
    public UserResponse criar(UserRequest request, String criadoPor) {
        log.info("Criando usuário: {}", request.username());

        User user = mapper.toEntity(request);
        user.setCriadoPor(criadoPor);
        user = repository.save(user);

        log.info("Usuário criado: id={}, username={}, role={}",
                user.getId(), user.getUsername(), user.getRole());
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

        // Valida se não está tentando alterar o último admin
        if (user.getRole() == Role.ADMIN && request.role() != Role.ADMIN) {
            validateNotLastAdmin(user);
        }

        mapper.updateEntityFromRequest(request, user);
        user = repository.save(user);

        log.info("Usuário atualizado: id={}, username={}", id, user.getUsername());
        return mapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        User user = findById(id);
        validateNotLastAdmin(user);

        repository.deleteById(id);
        log.info("Usuário deletado: id={}, username={}", id, user.getUsername());
    }

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

    @Override
    @Transactional
    public void ativar(Long id) {
        User user = findById(id);
        if (!user.getAtivo()) {
            user.setAtivo(true);
            user.setAtualizadoEm(LocalDateTime.now());
            repository.save(user);
            log.info("Usuário ativado: id={}, username={}", id, user.getUsername());
        }
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        User user = findById(id);
        validateNotLastAdmin(user);

        if (user.getAtivo()) {
            user.setAtivo(false);
            user.setAtualizadoEm(LocalDateTime.now());
            repository.save(user);
            log.info("Usuário inativado: id={}, username={}", id, user.getUsername());
        }
    }

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

    @Override
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("total", repository.count());
        stats.put("ativos", repository.countAtivos());
        stats.put("inativos", repository.count() - repository.countAtivos());

        stats.put("porRole", repository.countPorRole().stream()
                .collect(Collectors.toMap(r -> ((Role) r[0]).name(), r -> r[1])));

        return stats;
    }

    private User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    private void validateNotLastAdmin(User userToCheck) {
        if (userToCheck.getRole() == Role.ADMIN) {
            // Conta todos os admins ativos, excluindo o usuário que estamos verificando se for inativo
            long adminCount = repository.countAtivosByRole(Role.ADMIN);

            // Se o usuário está ativo e é admin, subtrai 1 do count
            if (userToCheck.getAtivo()) {
                adminCount--;
            }

            if (adminCount <= 0) {
                throw new BusinessException("Não é possível remover/inativar o último administrador ativo");
            }
        }
    }
}