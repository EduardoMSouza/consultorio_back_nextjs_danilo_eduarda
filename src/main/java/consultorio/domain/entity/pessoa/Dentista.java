package consultorio.domain.entity.pessoa;

import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.pessoa.enums.Especialidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um dentista no sistema de consultório odontológico.
 * Um dentista pode ter múltiplos agendamentos associados.
 *
 * @author Sistema Consultório
 * @since 1.0
 */
@Entity
@Table(name = "dentistas", indexes = {
        @Index(name = "idx_dentista_email", columnList = "email_dentista"),
        @Index(name = "idx_dentista_cro", columnList = "cro_dentista"),
        @Index(name = "idx_dentista_ativo", columnList = "ativo")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dentista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dentista")
    private Long id;

    @Column(name = "nome_dentista", nullable = false, length = 100)
    private String nome;

    @Column(name = "cro_dentista", nullable = false, unique = true, length = 10)
    private String cro;


    @Column(name = "especialidade_dentista", nullable = false, length = 50)
    private String especialidade;

    @Column(name = "telefone_dentista", nullable = false, length = 20)
    private String telefone;

    @Column(name = "email_dentista", nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "dentista",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Agendamento> agendamentos = new ArrayList<>();


    /**
     * Desativa o dentista no sistema.
     * Um dentista desativado não pode ser associado a novos agendamentos.
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Reativa o dentista no sistema.
     */
    public void ativar() {
        this.ativo = true;
    }

    /**
     * Verifica se o dentista está ativo no sistema.
     *
     * @return true se o dentista está ativo, false caso contrário
     */
    public boolean estaAtivo() {
        return Boolean.TRUE.equals(ativo);
    }



    /**
     * Define o email do dentista, garantindo que seja em letras minúsculas.
     *
     * @param email o email a ser definido
     */
    public void setEmail(String email) {
        if (email != null) {
            this.email = email.trim().toLowerCase();
        }
    }

    /**
     * Define o nome do dentista, removendo espaços extras.
     *
     * @param nome o nome a ser definido
     */
    public void setNome(String nome) {
        if (nome != null) {
            this.nome = nome.trim();
        }
    }

    /**
     * Define o telefone do dentista, removendo espaços extras.
     *
     * @param telefone o telefone a ser definido
     */
    public void setTelefone(String telefone) {
        if (telefone != null) {
            this.telefone = telefone.trim();
        }
    }
}