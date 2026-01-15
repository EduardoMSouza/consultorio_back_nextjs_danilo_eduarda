package consultorio.api.mapper.tratamento;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.PlanoDental;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável por converter entre DTOs e entidades de PlanoDental.
 * Implementação manual para garantir controle total sobre as conversões.
 *
 * @author Sistema Consultório
 * @since 1.0
 */
@Component
public class PlanoDentalMapper {

    public PlanoDental toEntity(PlanoDentalRequest request, Paciente paciente, Dentista dentista) {
        PlanoDental plano = new PlanoDental();

        // Relacionamentos
        plano.setPaciente(paciente);
        plano.setDentista(dentista);

        // Dados do dente
        plano.setDente(request.dente());
        plano.setFaceDente(request.faceDente());

        // Dados do procedimento
        plano.setProcedimento(request.procedimento());
        plano.setCodigoProcedimento(request.codigoProcedimento());

        // Valores financeiros
        plano.setValor(request.valor());
        plano.setValorDesconto(request.valorDesconto() != null ? request.valorDesconto() : BigDecimal.ZERO);
        // O valorFinal será calculado automaticamente pelo método calcularValorFinal()

        // Status e prioridade
        plano.setStatus(request.status());
        plano.setPrioridade(request.prioridade());
        plano.setUrgente(request.urgente());

        // Observações e datas
        plano.setObservacoes(request.observacoes());
        plano.setDataPrevista(request.dataPrevista());

        // Ativo por padrão
        plano.setAtivo(true);

        return plano;
    }

    public PlanoDentalResponse toResponse(PlanoDental entity) {
        if (entity == null) {
            return null;
        }

        PlanoDentalResponse response = new PlanoDentalResponse();

        // Dados básicos
        response.setId(entity.getId());

        // Dados do paciente
        if (entity.getPaciente() != null) {
            response.setPacienteId(entity.getPaciente().getId());
            response.setPacienteNome(entity.getPaciente().getNome());
        }

        // Dados do dentista
        if (entity.getDentista() != null) {
            response.setDentistaId(entity.getDentista().getId());
            response.setDentistaNome(entity.getDentista().getNome());
        }

        // Dados do dente
        response.setDente(entity.getDente());
        response.setFaceDente(entity.getFaceDente());

        // Dados do procedimento
        response.setProcedimento(entity.getProcedimento());
        response.setCodigoProcedimento(entity.getCodigoProcedimento());

        // Valores financeiros
        response.setValor(entity.getValor());
        response.setValorDesconto(entity.getValorDesconto());
        response.setValorFinal(entity.getValorFinal());

        // Status e prioridade
        response.setStatus(entity.getStatus());
        response.setPrioridade(entity.getPrioridade());
        response.setUrgente(entity.getUrgente());

        // Observações
        response.setObservacoes(entity.getObservacoes());
        response.setMotivoCancelamento(entity.getMotivoCancelamento());

        // Datas
        response.setDataPrevista(entity.getDataPrevista());
        response.setDataRealizacao(entity.getDataRealizacao());
        response.setDataCancelamento(entity.getDataCancelamento());

        // Metadados
        response.setAtivo(entity.getAtivo());
        response.setCriadoEm(entity.getCriadoEm());
        response.setAtualizadoEm(entity.getAtualizadoEm());

        return response;
    }

    public List<PlanoDentalResponse> toResponseList(List<PlanoDental> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(PlanoDentalRequest request, PlanoDental entity) {
        // Dados do dente
        if (request.dente() != null) {
            entity.setDente(request.dente());
        }

        if (request.faceDente() != null) {
            entity.setFaceDente(request.faceDente());
        }

        // Dados do procedimento
        if (request.procedimento() != null) {
            entity.setProcedimento(request.procedimento());
        }

        if (request.codigoProcedimento() != null) {
            entity.setCodigoProcedimento(request.codigoProcedimento());
        }

        // Valores financeiros - usar setters que recalculam valorFinal
        if (request.valor() != null) {
            entity.setValor(request.valor());
        }

        if (request.valorDesconto() != null) {
            entity.setValorDesconto(request.valorDesconto());
        } else {
            entity.setValorDesconto(BigDecimal.ZERO);
        }

        // Status e prioridade
        if (request.status() != null) {
            entity.setStatus(request.status());
        }

        if (request.prioridade() != null) {
            entity.setPrioridade(request.prioridade());
        }

        if (request.urgente() != null) {
            entity.setUrgente(request.urgente());
        }

        // Observações e data prevista
        entity.setObservacoes(request.observacoes());
        entity.setDataPrevista(request.dataPrevista());
    }

    public PlanoDental toEntityWithoutRelations(PlanoDentalRequest request) {
        return toEntity(request, null, null);
    }

    public void updateValores(PlanoDental entity, BigDecimal valor, BigDecimal valorDesconto) {
        if (valor != null) {
            entity.setValor(valor);
        }

        if (valorDesconto != null) {
            entity.setValorDesconto(valorDesconto);
        } else {
            entity.setValorDesconto(BigDecimal.ZERO);
        }
    }
}