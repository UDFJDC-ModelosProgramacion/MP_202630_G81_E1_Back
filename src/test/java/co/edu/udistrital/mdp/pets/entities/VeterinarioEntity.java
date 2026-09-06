package co.edu.udistrital.mdp.pets.entities;

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
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Represents a veterinarian hired by a shelter to look after the pets.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "veterinario")
public class VeterinarioEntity extends BaseEntity {

    private String nombre;
    private String telefono;
    private String email;
    private String especialidad;
    private String disponibilidad;

    // "contrata": many Veterinario are hired by one Refugio
    @PodamExclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refugio_id")
    private RefugioEntity refugio;

    // one Veterinario performs many Seguimiento
    @PodamExclude
    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeguimientoEntity> seguimientos = new ArrayList<>();

}
