package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MensajeEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.MensajeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MensajeService {

    private static final String MENSAJE_NO_EXISTE =
            "El mensaje con ID: %d no existe";

    private static final String ADOPTANTE_NO_EXISTE =
            "El adoptante con ID: %d no existe";

    private static final String MASCOTA_NO_EXISTE =
            "La mascota con ID: %d no existe";

    private static final String MENSAJE_NO_PERTENECE =
            "El mensaje con ID: %d no pertenece al adoptante con ID: %d";

    private static final String MENSAJE_YA_LEIDO =
            "El mensaje con ID: %d ya ha sido leído y no puede ser eliminado";

    private static final String MENSAJE_NO_EDITABLE =
            "El mensaje con ID: %d ya ha sido leído y no puede ser editado";

    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;
    private final MensajeRepository mensajeRepository;

    public MensajeService(
            MensajeRepository mensajeRepository,
            MascotaRepository mascotaRepository,
            AdoptanteRepository adoptanteRepository) {

        this.mascotaRepository = mascotaRepository;
        this.adoptanteRepository = adoptanteRepository;
        this.mensajeRepository = mensajeRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public List<MensajeEntity> getAllMensajes(Long idAdoptante) {

        try {
            validarAdoptante(idAdoptante);

            log.info(
                    "Listando todos los mensajes del adoptante con ID: {}",
                    idAdoptante);

            return mensajeRepository.findByAdoptanteId(idAdoptante);

        } catch (MensajeServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al listar los mensajes del adoptante con ID: {}",
                    idAdoptante,
                    e);

            throw new MensajeServiceException(
                    "Error al listar los mensajes del adoptante con ID: "
                            + idAdoptante,
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public List<MensajeEntity> getMensajesByMascotaId(Long idMascota) {

        try {
            validarMascota(idMascota);

            log.info(
                    "Listando todos los mensajes de la mascota con ID: {}",
                    idMascota);

            return mensajeRepository.findByMascotaId(idMascota);

        } catch (MensajeServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al listar los mensajes de la mascota con ID: {}",
                    idMascota,
                    e);

            throw new MensajeServiceException(
                    "Error al listar los mensajes de la mascota con ID: "
                            + idMascota,
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void eliminarMensaje(Long idMensaje, Long idAdoptante) {

        try {
            MensajeEntity mensaje =
                    obtenerMensaje(idMensaje);

            AdoptanteEntity adoptante =
                    obtenerAdoptante(idAdoptante);

            validarPropietario(mensaje, adoptante);

            if (mensaje.isLeido()) {
                log.warn(
                        "El mensaje con ID: {} ya ha sido leído y no puede ser eliminado",
                        idMensaje);

                throw new MensajeServiceException(
                        String.format(
                                MENSAJE_YA_LEIDO,
                                idMensaje));
            }

            log.info(
                    "Eliminando mensaje con ID: {}",
                    idMensaje);

            mensajeRepository.delete(mensaje);

        } catch (MensajeServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al eliminar el mensaje con ID: {}",
                    idMensaje,
                    e);

            throw new MensajeServiceException(
                    "Error al eliminar el mensaje con ID: "
                            + idMensaje,
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void marcarMensajeComoLeido(
            Long idMensaje,
            Long idAdoptante) {

        try {
            MensajeEntity mensaje =
                    obtenerMensaje(idMensaje);

            AdoptanteEntity adoptante =
                    obtenerAdoptante(idAdoptante);

            validarPropietario(mensaje, adoptante);

            mensaje.setLeido(true);
            mensajeRepository.save(mensaje);

            log.info(
                    "Marcando mensaje con ID: {} como leído",
                    idMensaje);

        } catch (MensajeServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al marcar el mensaje con ID: {} como leído",
                    idMensaje,
                    e);

            throw new MensajeServiceException(
                    "Error al marcar el mensaje con ID: "
                            + idMensaje
                            + " como leído",
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void marcarMensajeComoNoLeido(
            Long idMensaje,
            Long idAdoptante) {

        try {
            MensajeEntity mensaje =
                    obtenerMensaje(idMensaje);

            AdoptanteEntity adoptante =
                    obtenerAdoptante(idAdoptante);

            validarPropietario(mensaje, adoptante);

            mensaje.setLeido(false);
            mensajeRepository.save(mensaje);

            log.info(
                    "Marcando mensaje con ID: {} como no leído",
                    idMensaje);

        } catch (MensajeServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al marcar el mensaje con ID: {} como no leído",
                    idMensaje,
                    e);

            throw new MensajeServiceException(
                    "Error al marcar el mensaje con ID: "
                            + idMensaje
                            + " como no leído",
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void editarMensaje(
            MensajeEntity mensaje,
            Long idAdoptante) {

        try {
            MensajeEntity mensajeExistente =
                    obtenerMensaje(mensaje.getId());

            AdoptanteEntity adoptante =
                    obtenerAdoptante(idAdoptante);

            if (mensajeExistente.isLeido()) {
                log.warn(
                        "El mensaje con ID: {} ya ha sido leído y no puede ser editado",
                        mensaje.getId());

                throw new MensajeServiceException(
                        String.format(
                                MENSAJE_NO_EDITABLE,
                                mensaje.getId()));
            }

            validarPropietario(
                    mensajeExistente,
                    adoptante);

            mensaje.setAdoptante(
                    mensajeExistente.getAdoptante());

            log.info(
                    "Editando mensaje con ID: {}",
                    mensaje.getId());

            mensajeRepository.save(mensaje);

        } catch (MensajeServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al editar el mensaje con ID: {}",
                    mensaje.getId(),
                    e);

            throw new MensajeServiceException(
                    "Error al editar el mensaje con ID: "
                            + mensaje.getId(),
                    e);
        }
    }

    /**
     * Obtiene un mensaje y lanza una excepción específica
     * si no existe.
     */
    private MensajeEntity obtenerMensaje(Long idMensaje) {

        return mensajeRepository.findById(idMensaje)
                .orElseThrow(() -> {

                    log.warn(
                            "El mensaje con ID: {} no existe",
                            idMensaje);

                    return new MensajeServiceException(
                            String.format(
                                    MENSAJE_NO_EXISTE,
                                    idMensaje));
                });
    }

    /**
     * Obtiene un adoptante y lanza una excepción específica
     * si no existe.
     */
    private AdoptanteEntity obtenerAdoptante(Long idAdoptante) {

        return adoptanteRepository.findById(idAdoptante)
                .orElseThrow(() -> {

                    log.warn(
                            "El adoptante con ID: {} no existe",
                            idAdoptante);

                    return new MensajeServiceException(
                            String.format(
                                    ADOPTANTE_NO_EXISTE,
                                    idAdoptante));
                });
    }

    /**
     * Valida que el adoptante exista.
     */
    private void validarAdoptante(Long idAdoptante) {

        if (!adoptanteRepository.existsById(idAdoptante)) {

            log.warn(
                    "El adoptante con ID: {} no existe",
                    idAdoptante);

            throw new MensajeServiceException(
                    String.format(
                            ADOPTANTE_NO_EXISTE,
                            idAdoptante));
        }

        log.info(
                "El adoptante con ID: {} existe",
                idAdoptante);
    }

    /**
     * Valida que la mascota exista.
     */
    private void validarMascota(Long idMascota) {

        if (!mascotaRepository.existsById(idMascota)) {

            log.warn(
                    "La mascota con ID: {} no existe",
                    idMascota);

            throw new MensajeServiceException(
                    String.format(
                            MASCOTA_NO_EXISTE,
                            idMascota));
        }

        log.info(
                "La mascota con ID: {} existe",
                idMascota);
    }

    /**
     * Valida que el mensaje pertenezca al adoptante.
     */
    private void validarPropietario(
            MensajeEntity mensaje,
            AdoptanteEntity adoptante) {

        if (mensaje.getAdoptante() == null
                || adoptante == null
                || !mensaje.getAdoptante()
                        .getId()
                        .equals(adoptante.getId())) {

            log.warn(
                    "El mensaje con ID: {} no pertenece al adoptante con ID: {}",
                    mensaje.getId(),
                    adoptante.getId());

            throw new MensajeServiceException(
                    String.format(
                            MENSAJE_NO_PERTENECE,
                            mensaje.getId(),
                            adoptante.getId()));
        }
    }

    /**
     * Excepción específica para errores relacionados
     * con la gestión de mensajes.
     */
    public static class MensajeServiceException
            extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public MensajeServiceException(String message) {
            super(message);
        }

        public MensajeServiceException(
                String message,
                Throwable cause) {
            super(message, cause);
        }
    }
}
