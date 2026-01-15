package consultorio.api.mapper.pessoa;

import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import consultorio.api.dto.response.pessoa.embeddable.paciente.*;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.pessoa.embedded.paciente.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PacienteMapper {

    private final ModelMapper modelMapper;

    // ==================== REQUEST -> ENTITY ====================

    public Paciente toEntity(PacienteRequest request) {
        Paciente paciente = new Paciente();

        // Mapear DadosBasicos (obrigatório)
        if (request.getDadosBasicos() != null) {
            paciente.setDadosBasicos(modelMapper.map(request.getDadosBasicos(), DadosBasicos.class));
        }

        // Mapear Responsavel (opcional)
        if (request.getResponsavel() != null) {
            paciente.setResponsavel(modelMapper.map(request.getResponsavel(), Responsavel.class));
        }

        // Mapear Anamnese (opcional)
        if (request.getAnamnese() != null) {
            paciente.setAnamnese(modelMapper.map(request.getAnamnese(), Anamnese.class));
        }

        // Mapear Convenio (opcional)
        if (request.getConvenio() != null) {
            paciente.setConvenio(modelMapper.map(request.getConvenio(), Convenio.class));
        }

        // Mapear InspecaoBucal (opcional)
        if (request.getInspecaoBucal() != null) {
            paciente.setInspecaoBucal(modelMapper.map(request.getInspecaoBucal(), InspecaoBucal.class));
        }

        // Mapear QuestionarioSaude (opcional)
        if (request.getQuestionarioSaude() != null) {
            paciente.setQuestionarioSaude(modelMapper.map(request.getQuestionarioSaude(), QuestionarioSaude.class));
        }

        // Mapear observacoes
        paciente.setObservacoes(request.getObservacoes());

        // Ativo por padrão
        paciente.setAtivo(true);

        return paciente;
    }

    // ==================== ENTITY -> RESPONSE ====================

    public PacienteResponse toResponse(Paciente entity) {
        PacienteResponse response = new PacienteResponse();

        // Mapear campos diretos
        response.setId(entity.getId());
        response.setObservacoes(entity.getObservacoes());
        response.setAtivo(entity.getAtivo());
        response.setCriadoEm(entity.getCriadoEm());
        response.setAtualizadoEm(entity.getAtualizadoEm());

        // Mapear DadosBasicos
        if (entity.getDadosBasicos() != null) {
            DadosBasicosResponse dadosBasicosResponse = modelMapper.map(entity.getDadosBasicos(), DadosBasicosResponse.class);

            // Formatar CPF se existir
            if (dadosBasicosResponse.getCpf() != null && dadosBasicosResponse.getCpf().length() == 11) {
                String cpfFormatado = formatarCPF(dadosBasicosResponse.getCpf());
                // Você pode adicionar um setter para cpfFormatado se existir no DadosBasicosResponse
                // dadosBasicosResponse.setCpfFormatado(cpfFormatado);
            }

            response.setDadosBasicos(dadosBasicosResponse);

            // Calcular idade
            if (dadosBasicosResponse.getDataNascimento() != null) {
                response.setIdade(calcularIdade(dadosBasicosResponse.getDataNascimento()));
            }
        }

        // Mapear Responsavel
        if (entity.getResponsavel() != null) {
            ResponsavelResponse responsavelResponse = modelMapper.map(entity.getResponsavel(), ResponsavelResponse.class);

            // Formatar CPFs do responsável
            if (responsavelResponse.getCpf() != null && responsavelResponse.getCpf().length() == 11) {
                responsavelResponse.setCpfFormatado(formatarCPF(responsavelResponse.getCpf()));
            }

            if (responsavelResponse.getCpfConjuge() != null && responsavelResponse.getCpfConjuge().length() == 11) {
                responsavelResponse.setCpfConjugeFormatado(formatarCPF(responsavelResponse.getCpfConjuge()));
            }

            response.setResponsavel(responsavelResponse);
        }

        // Mapear outros componentes
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
        response.setAtivo(entity.getAtivo());

        // Mapear dados básicos
        if (entity.getDadosBasicos() != null) {
            DadosBasicos dados = entity.getDadosBasicos();
            response.setProntuarioNumero(dados.getProntuarioNumero());
            response.setNome(dados.getNome());
            response.setTelefone(dados.getTelefone());
            response.setCpf(dados.getCpf());
            response.setDataNascimento(dados.getDataNascimento());
            response.setStatus(dados.getStatus());

            // Formatar CPF
            if (dados.getCpf() != null && dados.getCpf().length() == 11) {
                response.setCpfFormatado(formatarCPF(dados.getCpf()));
            }

            // Calcular idade
            if (dados.getDataNascimento() != null) {
                response.setIdade(calcularIdade(dados.getDataNascimento()));
            }
        }

        // Mapear convênio
        if (entity.getConvenio() != null) {
            Convenio convenio = entity.getConvenio();
            response.setConvenio(convenio.getNomeConvenio());
            response.setNumeroInscricaoConvenio(convenio.getNumeroInscricao());
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
        // Atualizar DadosBasicos (parcialmente)
        if (request.getDadosBasicos() != null && entity.getDadosBasicos() != null) {
            modelMapper.map(request.getDadosBasicos(), entity.getDadosBasicos());
        }

        // Atualizar Responsavel
        if (request.getResponsavel() != null) {
            if (entity.getResponsavel() == null) {
                entity.setResponsavel(new Responsavel());
            }
            modelMapper.map(request.getResponsavel(), entity.getResponsavel());
        }

        // Atualizar Anamnese
        if (request.getAnamnese() != null) {
            if (entity.getAnamnese() == null) {
                entity.setAnamnese(new Anamnese());
            }
            modelMapper.map(request.getAnamnese(), entity.getAnamnese());
        }

        // Atualizar Convenio
        if (request.getConvenio() != null) {
            if (entity.getConvenio() == null) {
                entity.setConvenio(new Convenio());
            }
            modelMapper.map(request.getConvenio(), entity.getConvenio());
        }

        // Atualizar InspecaoBucal
        if (request.getInspecaoBucal() != null) {
            if (entity.getInspecaoBucal() == null) {
                entity.setInspecaoBucal(new InspecaoBucal());
            }
            modelMapper.map(request.getInspecaoBucal(), entity.getInspecaoBucal());
        }

        // Atualizar QuestionarioSaude
        if (request.getQuestionarioSaude() != null) {
            if (entity.getQuestionarioSaude() == null) {
                entity.setQuestionarioSaude(new QuestionarioSaude());
            }
            modelMapper.map(request.getQuestionarioSaude(), entity.getQuestionarioSaude());
        }

        // Atualizar observações (se fornecida)
        if (request.getObservacoes() != null) {
            entity.setObservacoes(request.getObservacoes());
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private Integer calcularIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return null;
        }
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
}