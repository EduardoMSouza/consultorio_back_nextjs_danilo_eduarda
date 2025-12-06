package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dentistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dentista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false) private String nome;
    private String cro;
    private String especialidade;
    private String telefone;
    private String email;
    private Boolean ativo = true;
}