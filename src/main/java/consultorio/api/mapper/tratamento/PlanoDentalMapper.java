package consultorio.api.mapper.tratamento;

import consultorio.api.dto.request.plano_dental.AtualizarPlanoDentalRequest;
import consultorio.api.dto.request.plano_dental.CriarPlanoDentalRequest;
import consultorio.api.dto.response.plano_dental.PlanoDentalDetalheResponse;
import consultorio.api.dto.response.plano_dental.PlanoDentalResponse;
import consultorio.api.dto.response.plano_dental.ResumoPlanoDentalResponse;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.PlanoDental;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PlanoDentalMapper {

    public PlanoDental toEntity(CriarPlanoDentalRequest request, Paciente paciente, Dentista dentista) {
        if (request == null) return null;

        BigDecimal valorFinal = request.getValorFinal() != null ? request.getValorFinal() : request.getValor();

        return PlanoDental.builder()
                .paciente(paciente)
                .dentista(dentista)
                .dente(request.getDente())
                .procedimento(request.getProcedimento())
                .valor(request.getValor())
                .valorFinal(valorFinal)
                .observacoes(request.getObservacoes())
                .build();
    }

    public void updateEntityFromDTO(AtualizarPlanoDentalRequest request, PlanoDental entity) {
        if (request == null || entity == null) return;

        if (request.getDente() != null) {
            entity.setDente(request.getDente());
        }
        if (request.getProcedimento() != null) {
            entity.setProcedimento(request.getProcedimento());
        }
        if (request.getValor() != null) {
            entity.setValor(request.getValor());
        }
        if (request.getValorFinal() != null) {
            entity.setValorFinal(request.getValorFinal());
        }
        if (request.getObservacoes() != null) {
            entity.setObservacoes(request.getObservacoes());
        }
    }

    public PlanoDentalResponse toResponse(PlanoDental entity) {
        if (entity == null) return null;

        BigDecimal valor = entity.getValor() != null ? entity.getValor() : BigDecimal.ZERO;
        BigDecimal valorFinal = entity.getValorFinal() != null ? entity.getValorFinal() : valor;
        BigDecimal desconto = valor.subtract(valorFinal);

        boolean temDesconto = desconto.compareTo(BigDecimal.ZERO) > 0;
        String percentualDesconto = "";

        if (temDesconto && valor.compareTo(BigDecimal.ZERO) > 0) {
            double percentual = desconto.divide(valor, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
            percentualDesconto = String.format("%.1f%%", percentual);
        }

        return PlanoDentalResponse.builder()
                .id(entity.getId())
                .pacienteId(entity.getPaciente() != null ? entity.getPaciente().getId() : null)
                .pacienteNome(entity.getPaciente() != null ? entity.getPaciente().getNome() : null)
                .pacienteTelefone(entity.getPaciente() != null ? entity.getPaciente().getTelefone() : null)
                .dentistaId(entity.getDentista() != null ? entity.getDentista().getId() : null)
                .dentistaNome(entity.getDentista() != null ? entity.getDentista().getNome() : null)
                .dentistaCro(entity.getDentista() != null ? entity.getDentista().getCro() : null)
                .dente(entity.getDente())
                .procedimento(entity.getProcedimento())
                .valor(valor)
                .valorFinal(valorFinal)
                .valorDesconto(desconto)
                .observacoes(entity.getObservacoes())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .temDesconto(temDesconto)
                .percentualDesconto(percentualDesconto)
                .build();
    }

    public PlanoDentalDetalheResponse toDetalheResponse(PlanoDental entity) {
        if (entity == null) return null;

        BigDecimal valor = entity.getValor() != null ? entity.getValor() : BigDecimal.ZERO;
        BigDecimal valorFinal = entity.getValorFinal() != null ? entity.getValorFinal() : valor;
        BigDecimal desconto = valor.subtract(valorFinal);

        Double percentualDesconto = 0.0;
        if (desconto.compareTo(BigDecimal.ZERO) > 0 && valor.compareTo(BigDecimal.ZERO) > 0) {
            percentualDesconto = desconto.divide(valor, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        // Verificar permissões (exemplo)
        boolean podeEditar = true; // Adicionar lógica real
        boolean podeExcluir = true;
        boolean podeAplicarDesconto = valor.compareTo(BigDecimal.ZERO) > 0;

        return PlanoDentalDetalheResponse.builder()
                .id(entity.getId())
                .pacienteId(entity.getPaciente() != null ? entity.getPaciente().getId() : null)
                .pacienteNome(entity.getPaciente() != null ? entity.getPaciente().getNome() : null)
                .pacienteTelefone(entity.getPaciente() != null ? entity.getPaciente().getTelefone() : null)
                .pacienteCpf(entity.getPaciente() != null ? entity.getPaciente().getCpf() : null)
                .dentistaId(entity.getDentista() != null ? entity.getDentista().getId() : null)
                .dentistaNome(entity.getDentista() != null ? entity.getDentista().getNome() : null)
                .dentistaCro(entity.getDentista() != null ? entity.getDentista().getCro() : null)
                .dentistaEspecialidade(entity.getDentista() != null ? entity.getDentista().getEspecialidade() : null)
                .dente(entity.getDente())
                .procedimento(entity.getProcedimento())
                .descricaoProcedimento(getDescricaoProcedimento(entity.getProcedimento()))
                .valor(valor)
                .valorFinal(valorFinal)
                .valorDesconto(desconto)
                .percentualDesconto(percentualDesconto)
                .observacoes(entity.getObservacoes())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .podeEditar(podeEditar)
                .podeExcluir(podeExcluir)
                .podeAplicarDesconto(podeAplicarDesconto)
                .valorFormatado(formatarMoeda(valor))
                .valorFinalFormatado(formatarMoeda(valorFinal))
                .valorDescontoFormatado(formatarMoeda(desconto))
                .build();
    }

    public ResumoPlanoDentalResponse toResumoResponse(PlanoDental entity) {
        if (entity == null) return null;

        BigDecimal valor = entity.getValor() != null ? entity.getValor() : BigDecimal.ZERO;
        BigDecimal valorFinal = entity.getValorFinal() != null ? entity.getValorFinal() : valor;
        boolean temDesconto = valorFinal.compareTo(valor) < 0;

        return ResumoPlanoDentalResponse.builder()
                .id(entity.getId())
                .pacienteNome(entity.getPaciente() != null ? entity.getPaciente().getNome() : null)
                .dentistaNome(entity.getDentista() != null ? entity.getDentista().getNome() : null)
                .dente(entity.getDente())
                .procedimento(entity.getProcedimento())
                .valor(valor)
                .valorFinal(valorFinal)
                .criadoEm(entity.getCriadoEm())
                .temDesconto(temDesconto)
                .build();
    }

    private String getDescricaoProcedimento(String procedimento) {
        // Adicionar mapeamento de procedimentos odontológicos
        if (procedimento == null) return "";

        switch (procedimento.toUpperCase()) {
            case "CONSULTA": return "Consulta odontológica";
            case "LIMPEZA": return "Limpeza dentária";
            case "EXTRACAO": return "Extração dentária";
            case "CANAL": return "Tratamento de canal";
            case "PROTESE": return "Prótese dentária";
            case "RESTAURACAO": return "Restauração dentária";
            default: return procedimento;
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor);
    }
}