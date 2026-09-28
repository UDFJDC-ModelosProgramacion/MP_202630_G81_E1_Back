package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

/**
 * DTO basico de Adoptante. No incluye "solicitudesAdopcion" ni "mensajes":
 * ambas son asociaciones de cardinalidad "*" y se exponen en
 * AdoptanteDTODetail.
 *
 * Nota: en Develop ya existe un AdoptanteDTO placeholder (creado por otro
 * integrante para MensajeDTODetail) con menos campos que este. Este es el
 * DTO completo del recurso Adoptante y debe reemplazar a ese placeholder.
 */
@Data
public class AdoptanteDTO {

    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String ciudad;
}
