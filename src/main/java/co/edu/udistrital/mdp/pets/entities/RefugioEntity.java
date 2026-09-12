package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Represents an animal shelter. A Refugio registers pets (Mascota) and
 * hires veterinarians (Veterinario) to take care of them.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "refugio")
public class RefugioEntity extends BaseEntity {

    private String nombre;
    private String ciudad;
    private String direccion;
    private String telefono;
    private String email;
    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    // "registra": one Refugio has many Mascota
    @PodamExclude
    @OneToMany(mappedBy = "refugio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MascotaEntity> mascotas = new ArrayList<>();

    // "contrata": one Refugio has many Veterinario
    @PodamExclude
    @OneToMany(mappedBy = "refugio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VeterinarioEntity> veterinarios = new ArrayList<>();

}
