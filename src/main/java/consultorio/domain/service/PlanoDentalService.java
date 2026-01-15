package consultorio.domain.service;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanoDentalService {

    PlanoDentalResponse create(PlanoDentalRequest request);

    PlanoDentalResponse findById(Long id);

    Page<PlanoDentalResponse> findAll(Pageable pageable);

    PlanoDentalResponse update(Long id, PlanoDentalRequest request);

    void delete(Long id);

    PlanoDentalResponse concluir(Long id);

    PlanoDentalResponse cancelar(Long id, String motivo);

    PlanoDentalResponse iniciar(Long id);

    PlanoDentalResponse aplicarDesconto(Long id, BigDecimal desconto);

    PlanoDentalResponse atualizarValor(Long id, BigDecimal valor);

    List<PlanoDentalResponse> findByPacienteId(Long pacienteId);

    Page<PlanoDentalResponse> findByPacienteId(Long pacienteId, Pageable pageable);

    List<PlanoDentalResponse> findByDentistaId(Long dentistaId);

    Page<PlanoDentalResponse> findByDentistaId(Long dentistaId, Pageable pageable);

    List<PlanoDentalResponse> findByStatus(StatusPlano status);

    Page<PlanoDentalResponse> findByStatus(StatusPlano status, Pageable pageable);

    List<PlanoDentalResponse> findAtivosByPacienteIdOrderByDataPrevista(Long pacienteId);

    List<PlanoDentalResponse> findByDataPrevistaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<PlanoDentalResponse> findUrgentesByStatus(StatusPlano status);

    List<PlanoDentalResponse> findByPacienteIdAndStatus(Long pacienteId, StatusPlano status);

    List<PlanoDentalResponse> findByDentistaIdAndStatus(Long dentistaId, StatusPlano status);

    BigDecimal calcularTotalPorPacienteAndStatus(Long pacienteId, StatusPlano status);

    Long countAtivosByPacienteId(Long pacienteId);

    List<PlanoDentalResponse> findByStatusIn(List<StatusPlano> statuses);

    PlanoDentalResponse ativar(Long id);

    List<PlanoDentalResponse> findByPacienteIdAndDentistaIdAndStatus(Long pacienteId, Long dentistaId, StatusPlano status);
}