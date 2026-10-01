package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

/**
 * DTO basico de RegistroVacunacion. Incluye "vacuna", "mascota" y
 * "seguimientoOrigen" porque, desde este recurso, las tres son asociaciones
 * de cardinalidad 1 (ManyToOne / 0..1) y por eso van en el DTO y no en el
 * detalle.
 *
 * Para crear o actualizar un registro, el cliente debe enviar al menos el id
 * de "vacuna" y de "mascota", por ejemplo: "vacuna": { "id": 3 },
 * "mascota": { "id": 7 }. "seguimientoOrigen" es opcional (cardinalidad 0..1).
 */
@Data
public class RegistroVacunacionDTO {

    private Long id;
    private Date fechaAplicacion;
    private Date proximaFecha;
    private String numeroLote;
    private String observacion;
    private VacunaDTO vacuna;
    private MascotaDTO mascota;
    private SeguimientoDTO seguimientoOrigen;
}
