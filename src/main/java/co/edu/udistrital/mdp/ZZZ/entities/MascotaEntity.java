package co.edu.udistrital.mdp.ZZZ.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a pet available for adoption at a shelter.
 *
 * Note: the full class diagram also links Mascota to Fotografia and
 * EventoVida, but those entities were assigned to other teammates, so
 * they are intentionally left out here and should be added when the
 * branches are merged.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mascota")
public class MascotaEntity extends BaseEntity {

    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private String tamano;
    private String temperamento;
    private String necesidadesEspecificas;
    private String requisitoEspacio;
    private Boolean compatibleConNinos;
    private Boolean compatibleConMascotas;
    private String nivelActividad;
    private String estado;

    // "registra": many Mascota belong to one Refugio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refugio_id")
    private RefugioEntity refugio;

    // a Mascota can have many Seguimiento (checkup follow-ups)
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeguimientoEntity> seguimientos = new ArrayList<>();

}
