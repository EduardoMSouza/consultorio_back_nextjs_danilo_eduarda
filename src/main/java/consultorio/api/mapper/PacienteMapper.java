package consultorio.api.mapper;


import consultorio.api.dto.request.PacienteRequest;
import consultorio.api.dto.response.PacienteResponse;
import consultorio.api.dto.response.PacienteResumoResponse;
import consultorio.api.dto.response.embeddable.paciente.*;
import consultorio.domain.entity.Paciente;
import consultorio.domain.entity.emededdable.paciente.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PacienteMapper {

    private final ModelMapper modelMapper;

    // ==================== REQUEST -> ENTITY ====================

    public Paciente toEntity(PacienteRequest request) {
        Paciente paciente = new Paciente();

        if (request.getDadosBasicos() != null) {
            paciente.setDadosBasicos(modelMapper.map(request.getDadosBasicos(), DadosBasicos.class));
        }
        if (request.getResponsavel() != null) {
            paciente.setResponsavel(modelMapper.map(request.getResponsavel(), Responsavel.class));
        }
        if (request.getAnamnese() != null) {
            paciente.setAnamnese(modelMapper.map(request.getAnamnese(), Anamnese.class));
        }
        if (request.getConvenio() != null) {
            paciente.setConvenio(modelMapper.map(request.getConvenio(), Convenio.class));
        }
        if (request.getInspecaoBucal() != null) {
            paciente.setInspecaoBucal(modelMapper.map(request.getInspecaoBucal(), InspecaoBucal.class));
        }
        if (request.getQuestionarioSaude() != null) {
            paciente.setQuestionarioSaude(modelMapper.map(request.getQuestionarioSaude(), QuestionarioSaude.class));
        }

        return paciente;
    }

    // ==================== ENTITY -> RESPONSE ====================

    public PacienteResponse toResponse(Paciente entity) {
        PacienteResponse response = new PacienteResponse();

        response.setId(entity.getId());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getDadosBasicos() != null) {
            response.setDadosBasicos(modelMapper.map(entity.getDadosBasicos(), DadosBasicosResponse.class));
        }
        if (entity.getResponsavel() != null) {
            response.setResponsavel(modelMapper.map(entity.getResponsavel(), ResponsavelResponse.class));
        }
        if (entity.getAnamnese() != null) {
            response.setAnamnese(modelMapper.map(entity.getAnamnese(), AnamneseResponse.class));
        }
        if (entity.getConvenio() != null) {
            response.setConvenio(modelMapper.map(entity.getConvenio(), ConvenioResponse.class));
        }
        if (entity.getInspecaoBucal() != null) {
            response.setInspecaoBucal(modelMapper.map(entity.getInspecaoBucal(), InspecaoBucalResponse.class));
        }
        if (entity.getQuestionarioSaude() != null) {
            response.setQuestionarioSaude(modelMapper.map(entity.getQuestionarioSaude(), QuestionarioSaudeResponse.class));
        }

        return response;
    }

    public List<PacienteResponse> toResponseList(List<Paciente> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================== ENTITY -> RESUMO ====================

    public PacienteResumoResponse toResumoResponse(Paciente entity) {
        PacienteResumoResponse response = new PacienteResumoResponse();

        response.setId(entity.getId());

        if (entity.getDadosBasicos() != null) {
            response.setProntuarioNumero(entity.getDadosBasicos().getProntuarioNumero());
            response.setNome(entity.getDadosBasicos().getNome());
            response.setTelefone(entity.getDadosBasicos().getTelefone());
            response.setCpf(entity.getDadosBasicos().getCpf());
            response.setDataNascimento(entity.getDadosBasicos().getDataNascimento());
            response.setStatus(entity.getDadosBasicos().getStatus());
        }

        if (entity.getConvenio() != null) {
            response.setConvenio(entity.getConvenio().getNome());
        }

        return response;
    }

    public List<PacienteResumoResponse> toResumoResponseList(List<Paciente> entities) {
        return entities.stream()
                .map(this::toResumoResponse)
                .toList();
    }

    // ==================== UPDATE ENTITY ====================

    public void updateEntityFromRequest(PacienteRequest request, Paciente entity) {
        if (request.getDadosBasicos() != null) {
            modelMapper.map(request.getDadosBasicos(), entity.getDadosBasicos());
        }
        if (request.getResponsavel() != null) {
            modelMapper.map(request.getResponsavel(), entity.getResponsavel());
        }
        if (request.getAnamnese() != null) {
            modelMapper.map(request.getAnamnese(), entity.getAnamnese());
        }
        if (request.getConvenio() != null) {
            modelMapper.map(request.getConvenio(), entity.getConvenio());
        }
        if (request.getInspecaoBucal() != null) {
            modelMapper.map(request.getInspecaoBucal(), entity.getInspecaoBucal());
        }
        if (request.getQuestionarioSaude() != null) {
            modelMapper.map(request.getQuestionarioSaude(), entity.getQuestionarioSaude());
        }
    }
}