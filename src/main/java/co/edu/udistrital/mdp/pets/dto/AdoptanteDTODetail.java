package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO detallado de Adoptante. Agrega las colecciones asociadas
 * (cardinalidad "*"): mensajes y solicitudes de adopcion.
 *
 * "solicitudesAdopcion" (List<SolicitudAdopcionDTO>) queda pendiente de
 * agregar: a la fecha SolicitudAdopcionDTO todavia no existe en ninguna
 * rama del equipo (le corresponde al responsable de ese recurso subirlo).
 * En cuanto exista, agregar aqui:
 *   private List<SolicitudAdopcionDTO> solicitudesAdopcion = new ArrayList<>();
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdoptanteDTODetail extends AdoptanteDTO {

    private List<MensajeDTO> mensajes = new ArrayList<>();
}
