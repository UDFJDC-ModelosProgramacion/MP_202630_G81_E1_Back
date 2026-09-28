package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Detalle de Refugio: agrega las asociaciones de cardinalidad "muchos"
 * (mascotas que registra y veterinarios que contrata).
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RefugioDetailDTO extends RefugioDTO {

    private List<MascotaDTO> mascotas = new ArrayList<>();
    private List<VeterinarioDTO> veterinarios = new ArrayList<>();
}
