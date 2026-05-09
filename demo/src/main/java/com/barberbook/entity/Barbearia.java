package com.barberbook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "barbearias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"barbeiros", "servicos", "horariosFuncionamento"})
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    private String telefone;

    @Column(columnDefinition = "TEXT")
    private String endereco;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(nullable = false)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @OneToMany(mappedBy = "barbearia", fetch = FetchType.LAZY)
    private List<Barbeiro> barbeiros;

    @OneToMany(mappedBy = "barbearia", fetch = FetchType.LAZY)
    private List<Servico> servicos;

    @OneToMany(mappedBy = "barbearia", fetch = FetchType.LAZY)
    private List<HorarioFuncionamento> horariosFuncionamento;

    @PrePersist
    protected void onCreate() {
        criadoEm = OffsetDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Barbearia other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
