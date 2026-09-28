package co.edu.udistrital.mdp.pets.services;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.EventoVidaEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.EventoVidaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventoVidaService {

    private static final String ADOPTANTE_NO_EXISTE =
            "Adoptante with id %d does not exist";

    private static final String TIPO_EVENTO_VACIO =
            "tipoEvento cannot be null or empty";

    private static final String TIPO_EVENTO_INVALIDO =
            "Invalid tipoEvento: %s";

    private static final String FECHA_EVENTO_VACIA =
            "fechaEvento cannot be null or empty";

    private static final String FECHA_EVENTO_FUTURA =
            "fechaEvento cannot be in the future";

    private static final List<String> VALID_EVENT_TYPES =
            List.of("Nacimiento", "Vacunación", "Enfermedad", "Adopción", "Muerte");

    private final AdoptanteRepository adoptanteRepository;
    private final EventoVidaRepository eventoVidaRepository;

    EventoVidaService(EventoVidaRepository eventoVidaRepository,
            AdoptanteRepository adoptanteRepository) {
        this.eventoVidaRepository = eventoVidaRepository;
        this.adoptanteRepository = adoptanteRepository;
    }

    // Fetch all EventoVida entities for a given mascotaId
    @Transactional(rollbackOn = Exception.class)
    public List<EventoVidaEntity> getEventosVidaByMascotaId(Long id) {
        try {
            // Check if the adoptante exists before fetching events
            if (!adoptanteRepository.existsById(id)) {
                log.warn("Adoptante with id {} does not exist", id);
                throw new IllegalArgumentException(
                        String.format(ADOPTANTE_NO_EXISTE, id));
            }

            log.info("Fetching all EventoVida entities");
            return eventoVidaRepository.findByMascotaId(id);
        } catch (Exception e) {
            log.error("Error fetching EventoVida entities: {}", e.getMessage());
            throw e;
        }
    }

    // Fetch EventoVida entities for a given mascotaId and tipoEvento
    @Transactional(rollbackOn = Exception.class)
    public List<EventoVidaEntity> getEventosVidaByMascotaIdAndTipoEvento(
            Long id, String tipoEvento) {
        try {
            // Check if the adoptante exists before fetching events
            if (!adoptanteRepository.existsById(id)) {
                log.warn("Adoptante with id {} does not exist", id);
                throw new IllegalArgumentException(
                        String.format(ADOPTANTE_NO_EXISTE, id));
            }

            // Validate tipoEvento parameter
            if (tipoEvento == null || tipoEvento.isEmpty()) {
                log.warn("tipoEvento is null or empty");
                throw new IllegalArgumentException(TIPO_EVENTO_VACIO);
            }

            // Validate if tipoEvento is one of the valid event types
            if (!VALID_EVENT_TYPES.contains(tipoEvento)) {
                log.warn("Invalid tipoEvento: {}", tipoEvento);
                throw new IllegalArgumentException(
                        String.format(TIPO_EVENTO_INVALIDO, tipoEvento));
            }

            log.info(
                    "Fetching EventoVida entities by mascotaId: {} and tipoEvento: {}",
                    id, tipoEvento);

            return eventoVidaRepository.findByMascotaIdAndTipo(id, tipoEvento);
        } catch (Exception e) {
            log.error("Error fetching EventoVida entities: {}", e.getMessage());
            throw e;
        }
    }

    // Fetch EventoVida entities for a given mascotaId, tipoEvento, and fechaEvento between two dates
    @Transactional(rollbackOn = Exception.class)
    public List<EventoVidaEntity> getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoBetween(
            Long id, String tipoEvento, Date fechaInicio, Date fechaFin) {
        try {
            // Check if the adoptante exists before fetching events
            if (!adoptanteRepository.existsById(id)) {
                log.warn("Adoptante with id {} does not exist", id);
                throw new IllegalArgumentException(
                        String.format(ADOPTANTE_NO_EXISTE, id));
            }

            // Validate tipoEvento parameter
            if (tipoEvento == null || tipoEvento.isEmpty()) {
                log.warn("tipoEvento is null or empty");
                throw new IllegalArgumentException(TIPO_EVENTO_VACIO);
            }

            // Validate if tipoEvento is one of the valid event types
            if (!VALID_EVENT_TYPES.contains(tipoEvento)) {
                log.warn("Invalid tipoEvento: {}", tipoEvento);
                throw new IllegalArgumentException(
                        String.format(TIPO_EVENTO_INVALIDO, tipoEvento));
            }

            log.info(
                    "Fetching EventoVida entities by mascotaId: {}, tipoEvento: {}, fechaInicio: {}, fechaFin: {}",
                    id, tipoEvento, fechaInicio, fechaFin);

            return eventoVidaRepository.findByMascotaIdAndTipoAndFechaBetween(
                    id, tipoEvento, fechaInicio, fechaFin);
        } catch (Exception e) {
            log.error("Error fetching EventoVida entities: {}", e.getMessage());
            throw e;
        }
    }

    // Fetch EventoVida entities for a given mascotaId, tipoEvento, and fechaEvento after a specific date
    @Transactional(rollbackOn = Exception.class)
    public List<EventoVidaEntity> getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoAfter(
            Long id, String tipoEvento, Date fechaInicio) {
        try {
            // Check if the adoptante exists before fetching events
            if (!adoptanteRepository.existsById(id)) {
                log.warn("Adoptante with id {} does not exist", id);
                throw new IllegalArgumentException(
                        String.format(ADOPTANTE_NO_EXISTE, id));
            }

            // Validate tipoEvento parameter
            if (tipoEvento == null || tipoEvento.isEmpty()) {
                log.warn("tipoEvento is null or empty");
                throw new IllegalArgumentException(TIPO_EVENTO_VACIO);
            }

            // Validate if tipoEvento is one of the valid event types
            if (!VALID_EVENT_TYPES.contains(tipoEvento)) {
                log.warn("Invalid tipoEvento: {}", tipoEvento);
                throw new IllegalArgumentException(
                        String.format(TIPO_EVENTO_INVALIDO, tipoEvento));
            }

            log.info(
                    "Fetching EventoVida entities by mascotaId: {}, tipoEvento: {}, fechaInicio: {}",
                    id, tipoEvento, fechaInicio);

            return eventoVidaRepository.findByMascotaIdAndTipoAndFechaAfter(
                    id, tipoEvento, fechaInicio);
        } catch (Exception e) {
            log.error("Error fetching EventoVida entities: {}", e.getMessage());
            throw e;
        }
    }

    // Create a new EventoVida entity
    @Transactional(rollbackOn = Exception.class)
    public void createEventoVida(EventoVidaEntity eventoVida) {
        try {
            // Validate tipoEvento parameter
            if (eventoVida.getTipo() == null || eventoVida.getTipo().isEmpty()) {
                log.warn("tipoEvento is null or empty");
                throw new IllegalArgumentException(TIPO_EVENTO_VACIO);
            }

            // Validate if tipoEvento is one of the valid event types
            if (!VALID_EVENT_TYPES.contains(eventoVida.getTipo())) {
                log.warn("Invalid tipoEvento: {}", eventoVida.getTipo());
                throw new IllegalArgumentException(
                        String.format(TIPO_EVENTO_INVALIDO, eventoVida.getTipo()));
            }

            // Validate fechaEvento parameter
            if (eventoVida.getFecha() == null) {
                log.warn("fechaEvento is null or empty");
                throw new IllegalArgumentException(FECHA_EVENTO_VACIA);
            }

            if (!eventoVida.getFecha().before(new Date(System.currentTimeMillis()))) {
                log.warn("fechaEvento is in the future: {}", eventoVida.getFecha());
                throw new IllegalArgumentException(FECHA_EVENTO_FUTURA);
            }

            log.info("Creating EventoVida entity: {}", eventoVida);
            eventoVidaRepository.save(eventoVida);
        } catch (Exception e) {
            log.error("Error creating EventoVida entity: {}", e.getMessage());
            throw e;
        }
    }

    // Update an existing EventoVida entity
    @Transactional(rollbackOn = Exception.class)
    public void updateEventoVida(EventoVidaEntity eventoVida) {
        try {
            // Validate tipoEvento parameter
            if (eventoVida.getTipo() == null || eventoVida.getTipo().isEmpty()) {
                log.warn("tipoEvento is null or empty");
                throw new IllegalArgumentException(TIPO_EVENTO_VACIO);
            }

            // Validate if tipoEvento is one of the valid event types
            if (!VALID_EVENT_TYPES.contains(eventoVida.getTipo())) {
                log.warn("Invalid tipoEvento: {}", eventoVida.getTipo());
                throw new IllegalArgumentException(
                        String.format(TIPO_EVENTO_INVALIDO, eventoVida.getTipo()));
            }

            // Validate fechaEvento parameter
            if (eventoVida.getFecha() == null) {
                log.warn("fechaEvento is null or empty");
                throw new IllegalArgumentException(FECHA_EVENTO_VACIA);
            }

            if (!eventoVida.getFecha().before(new Date(System.currentTimeMillis()))) {
                log.warn("fechaEvento is in the future: {}", eventoVida.getFecha());
                throw new IllegalArgumentException(FECHA_EVENTO_FUTURA);
            }

            log.info("Updating EventoVida entity: {}", eventoVida);
            eventoVidaRepository.save(eventoVida);
        } catch (Exception e) {
            log.error("Error updating EventoVida entity: {}", e.getMessage());
            throw e;
        }
    }

    // Delete an existing EventoVida entity
    @Transactional(rollbackOn = Exception.class)
    public void deleteEventoVida(Long id) {
        try {
            log.info("Deleting EventoVida entity with id: {}", id);
            eventoVidaRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting EventoVida entity: {}", e.getMessage());
            throw e;
        }
    }
}
