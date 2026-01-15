package consultorio.api.mapper.tratamento;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.PlanoDental;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanoDentalMapper {

    // ========== REQUEST TO ENTITY (CREATE) ==========

    public PlanoDental toEntity(PlanoDentalRequest request) {
        if (request == null) {
            return null;
        }

        return PlanoDental.builder()
                .dente(request.getDente())
                .faceDente(request.getFaceDente())
                .procedimento(request.getProcedimento())
                .codigoProcedimento(request.getCodigoProcedimento())
                .valor(request.getValor())
                .valorDesconto(request.getValorDesconto() != null ? request.getValorDesconto() : java.math.BigDecimal.ZERO)
                .status(request.getStatus() != null ? request.getStatus() : consultorio.domain.entity.tratamento.enums.StatusPlano.PENDENTE)
                .prioridade(request.getPrioridade() != null ? request.getPrioridade() : "NORMAL")
                .urgente(request.getUrgente() != null ? request.getUrgente() : false)
                .observacoes(request.getObservacoes())
                .dataPrevista(request.getDataPrevista())
                .ativo(true)
                .build();
    }

    // ========== REQUEST TO ENTITY WITH RELATIONSHIPS ==========

    public PlanoDental toEntity(PlanoDentalRequest request, Paciente paciente, Dentista dentista) {
        if (request == null) {
            return null;
        }

        PlanoDental plano = toEntity(request);
        plano.setPaciente(paciente);
        plano.setDentista(dentista);
        plano.calcularValorFinal();

        return plano;
    }

    // ========== ENTITY TO RESPONSE ==========

    public PlanoDentalResponse toResponse(PlanoDental entity) {
        if (entity == null) {
            return null;
        }

        PlanoDentalResponse response = new PlanoDentalResponse();

        // IDs e informações básicas
        response.setId(entity.getId());
        response.setDente(entity.getDente());
        response.setFaceDente(entity.getFaceDente());
        response.setProcedimento(entity.getProcedimento());
        response.setCodigoProcedimento(entity.getCodigoProcedimento());
        response.setValor(entity.getValor());
        response.setValorDesconto(entity.getValorDesconto());
        response.setValorFinal(entity.getValorFinal());
        response.setStatus(entity.getStatus());
        response.setPrioridade(entity.getPrioridade());
        response.setUrgente(entity.getUrgente());
        response.setObservacoes(entity.getObservacoes());
        response.setMotivoCancelamento(entity.getMotivoCancelamento());
        response.setDataPrevista(entity.getDataPrevista());
        response.setDataRealizacao(entity.getDataRealizacao());
        response.setDataCancelamento(entity.getDataCancelamento());
        response.setAtivo(entity.getAtivo());
        response.setCriadoEm(entity.getCriadoEm());
        response.setAtualizadoEm(entity.getAtualizadoEm());

        // Relacionamentos - Paciente
        if (entity.getPaciente() != null) {
            response.setPacienteId(entity.getPaciente().getId());
            response.setPacienteNome(entity.getPaciente().getNome());
        }

        // Relacionamentos - Dentista
        if (entity.getDentista() != null) {
            response.setDentistaId(entity.getDentista().getId());
            response.setDentistaNome(entity.getDentista().getNome());
        }

        return response;
    }

    // ========== UPDATE ENTITY FROM REQUEST ==========

    public void updateEntityFromRequest(PlanoDentalRequest request, PlanoDental entity) {
        if (request == null || entity == null) {
            return;
        }

        // Campos básicos
        if (request.getDente() != null && !request.getDente().trim().isEmpty()) {
            entity.setDente(request.getDente().trim().toUpperCase());
        }

        if (request.getFaceDente() != null) {
            entity.setFaceDente(request.getFaceDente().trim());
        }

        if (request.getProcedimento() != null && !request.getProcedimento().trim().isEmpty()) {
            entity.setProcedimento(request.getProcedimento().trim());
        }

        if (request.getCodigoProcedimento() != null) {
            entity.setCodigoProcedimento(request.getCodigoProcedimento().trim());
        }

        if (request.getValor() != null) {
            entity.setValor(request.getValor());
        }

        if (request.getValorDesconto() != null) {
            entity.setValorDesconto(request.getValorDesconto());
        }

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        if (request.getPrioridade() != null && !request.getPrioridade().trim().isEmpty()) {
            entity.setPrioridade(request.getPrioridade().trim());
        }

        if (request.getUrgente() != null) {
            entity.setUrgente(request.getUrgente());
        }

        if (request.getObservacoes() != null) {
            entity.setObservacoes(request.getObservacoes().trim());
        }

        if (request.getDataPrevista() != null) {
            entity.setDataPrevista(request.getDataPrevista());
        }

        // Recalcular valor final
        entity.calcularValorFinal();
        entity.setAtualizadoEm(java.time.LocalDateTime.now());
    }

    // ========== UPDATE ENTITY FROM REQUEST WITH RELATIONSHIPS ==========

    public void updateEntityFromRequest(PlanoDentalRequest request, PlanoDental entity,
                                        Paciente paciente, Dentista dentista) {
        updateEntityFromRequest(request, entity);

        // Atualizar relacionamentos se fornecidos
        if (paciente != null) {
            entity.setPaciente(paciente);
        }
        if (dentista != null) {
            entity.setDentista(dentista);
        }
    }

    // ========== UPDATE PARTIAL (PATCH) ==========

    public void updateEntityPartial(PlanoDentalRequest request, PlanoDental entity) {
        if (request == null || entity == null) {
            return;
        }

        // Apenas atualiza campos que não são nulos no request
        if (request.getDente() != null && !request.getDente().trim().isEmpty()) {
            entity.setDente(request.getDente().trim().toUpperCase());
        }

        if (request.getFaceDente() != null) {
            entity.setFaceDente(request.getFaceDente().trim());
        }

        if (request.getProcedimento() != null && !request.getProcedimento().trim().isEmpty()) {
            entity.setProcedimento(request.getProcedimento().trim());
        }

        if (request.getCodigoProcedimento() != null && !request.getCodigoProcedimento().trim().isEmpty()) {
            entity.setCodigoProcedimento(request.getCodigoProcedimento().trim());
        }

        if (request.getValor() != null) {
            entity.setValor(request.getValor());
        }

        if (request.getValorDesconto() != null) {
            entity.setValorDesconto(request.getValorDesconto());
        }

        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        if (request.getPrioridade() != null && !request.getPrioridade().trim().isEmpty()) {
            entity.setPrioridade(request.getPrioridade().trim());
        }

        if (request.getUrgente() != null) {
            entity.setUrgente(request.getUrgente());
        }

        if (request.getObservacoes() != null) {
            entity.setObservacoes(request.getObservacoes().trim());
        }

        if (request.getDataPrevista() != null) {
            entity.setDataPrevista(request.getDataPrevista());
        }

        // Recalcular valor final
        entity.calcularValorFinal();
        entity.setAtualizadoEm(java.time.LocalDateTime.now());
    }

    // ========== LIST TO RESPONSE LIST ==========

    public java.util.List<PlanoDentalResponse> toResponseList(java.util.List<PlanoDental> entities) {
        if (entities == null) {
            return java.util.Collections.emptyList();
        }

        return entities.stream()
                .map(this::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    // ========== RESPONSE TO ENTITY (RARAMENTE USADO) ==========

    public PlanoDental responseToEntity(PlanoDentalResponse response) {
        if (response == null) {
            return null;
        }

        return PlanoDental.builder()
                .id(response.getId())
                .dente(response.getDente())
                .faceDente(response.getFaceDente())
                .procedimento(response.getProcedimento())
                .codigoProcedimento(response.getCodigoProcedimento())
                .valor(response.getValor())
                .valorDesconto(response.getValorDesconto())
                .valorFinal(response.getValorFinal())
                .status(response.getStatus())
                .prioridade(response.getPrioridade())
                .urgente(response.getUrgente())
                .observacoes(response.getObservacoes())
                .motivoCancelamento(response.getMotivoCancelamento())
                .dataPrevista(response.getDataPrevista())
                .dataRealizacao(response.getDataRealizacao())
                .dataCancelamento(response.getDataCancelamento())
                .ativo(response.getAtivo())
                .criadoEm(response.getCriadoEm())
                .atualizadoEm(response.getAtualizadoEm())
                .build();
    }

    // ========== MÉTODOS AUXILIARES PARA IDs ==========

    public Paciente mapPacienteIdToEntity(Long pacienteId) {
        if (pacienteId == null) {
            return null;
        }
        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        return paciente;
    }

    public Dentista mapDentistaIdToEntity(Long dentistaId) {
        if (dentistaId == null) {
            return null;
        }
        Dentista dentista = new Dentista();
        dentista.setId(dentistaId);
        return dentista;
    }

    // ========== MÉTODO PARA DTO SIMPLIFICADO (PARA LISTAGENS) ==========

    public PlanoDentalResponse toSummaryResponse(PlanoDental entity) {
        if (entity == null) {
            return null;
        }

        PlanoDentalResponse summary = new PlanoDentalResponse();

        summary.setId(entity.getId());
        summary.setDente(entity.getDente());
        summary.setFaceDente(entity.getFaceDente());
        summary.setProcedimento(entity.getProcedimento());
        summary.setValorFinal(entity.getValorFinal());
        summary.setStatus(entity.getStatus());
        summary.setPrioridade(entity.getPrioridade());
        summary.setUrgente(entity.getUrgente());
        summary.setDataPrevista(entity.getDataPrevista());
        summary.setAtivo(entity.getAtivo());
        summary.setCriadoEm(entity.getCriadoEm());

        // Resumo das observações
        if (entity.getObservacoes() != null && entity.getObservacoes().length() > 100) {
            summary.setObservacoes(entity.getObservacoes().substring(0, 100) + "...");
        } else {
            summary.setObservacoes(entity.getObservacoes());
        }

        // Relacionamentos básicos
        if (entity.getPaciente() != null) {
            summary.setPacienteId(entity.getPaciente().getId());
            summary.setPacienteNome(entity.getPaciente().getNome());
        }

        if (entity.getDentista() != null) {
            summary.setDentistaId(entity.getDentista().getId());
            summary.setDentistaNome(entity.getDentista().getNome());
        }

        return summary;
    }

    // ========== MÉTODO PARA FORMATAR CAMPOS DO REQUEST ==========

    public void formatarCamposRequest(PlanoDentalRequest request) {
        if (request == null) {
            return;
        }

        if (request.getDente() != null) {
            request.setDente(request.getDente().trim().toUpperCase());
        }

        if (request.getFaceDente() != null) {
            request.setFaceDente(request.getFaceDente().trim());
        }

        if (request.getProcedimento() != null) {
            request.setProcedimento(request.getProcedimento().trim());
        }

        if (request.getCodigoProcedimento() != null) {
            request.setCodigoProcedimento(request.getCodigoProcedimento().trim());
        }

        if (request.getPrioridade() != null) {
            request.setPrioridade(request.getPrioridade().trim());
        }

        if (request.getObservacoes() != null) {
            request.setObservacoes(request.getObservacoes().trim());
        }
    }
}