package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.repositories.HistoriaExitoRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HistoriaExitoService {

    private static final String MASCOTA_NO_EXISTE =
            "La mascota con ID: %d no existe";

    private static final String MASCOTA_INACTIVA =
            "La mascota con ID: %d se encuentra inactiva";

    private static final String HISTORIA_NO_EXISTE =
            "La historia de éxito con ID asociada a la mascota seleccionada: %d no existe";

    private static final String ERROR_ELIMINAR =
            "Error al eliminar la historia de éxito con ID: %d";

    private static final String ERROR_CREAR =
            "Error al crear la historia de éxito para la mascota con ID: %d";

    private static final String ERROR_ACTUALIZAR =
            "Error al actualizar la historia de éxito con ID: %d para la mascota con ID: %d";

    private static final String ERROR_OBTENER =
            "Error al obtener la historia de éxito con ID: %d para la mascota con ID: %d";

    private static final String ERROR_OBTENER_TODAS =
            "Error al obtener las historias de éxito para la mascota con ID: %d";

    private final HistoriaExitoRepository historiaExitoRepository;
    private final MascotaRepository mascotaRepository;

    HistoriaExitoService(
            HistoriaExitoRepository historiaExitoRepository,
            MascotaRepository mascotaRepository) {
        this.historiaExitoRepository = historiaExitoRepository;
        this.mascotaRepository = mascotaRepository;
    }

    // Método para eliminar una historia de éxito asociada a una mascota
    @Transactional(rollbackOn = Exception.class)
    public void eliminarHistoriaExito(Long idHistoriaExito, Long idMascota) {
        try {
            validarMascotaActiva(idMascota);

            HistoriaExitoEntity historiaExito =
                    historiaExitoRepository.findByMascotaIdAndId(
                            idMascota,
                            idHistoriaExito);

            if (historiaExito == null) {
                log.warn(
                        "La historia de éxito con ID: {} no existe",
                        idHistoriaExito);

                throw new HistoriaExitoException(
                        String.format(HISTORIA_NO_EXISTE, idHistoriaExito));
            }

            log.info(
                    "La historia de éxito con ID asociada a la mascota seleccionada: {} existe",
                    idHistoriaExito);

            log.info(
                    "Eliminando historia de éxito con ID: {}",
                    idHistoriaExito);

            historiaExitoRepository.deleteById(idHistoriaExito);

        } catch (HistoriaExitoException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al eliminar la historia de éxito con ID: {}",
                    idHistoriaExito,
                    e);

            throw new HistoriaExitoException(
                    String.format(ERROR_ELIMINAR, idHistoriaExito),
                    e);
        }
    }

    // Método para crear una historia de éxito asociada a una mascota
    @Transactional(rollbackOn = Exception.class)
    public void crearHistoriaExito(
            Long idMascota,
            String titulo,
            String descripcion) {

        try {
            MascotaEntity mascota = validarMascotaActiva(idMascota);

            HistoriaExitoEntity historiaExito =
                    new HistoriaExitoEntity();

            historiaExito.setTitulo(titulo);
            historiaExito.setDescripcion(descripcion);
            historiaExito.setMascota(mascota);

            historiaExitoRepository.save(historiaExito);

            log.info(
                    "Historia de éxito creada exitosamente para la mascota con ID: {}",
                    idMascota);

        } catch (HistoriaExitoException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al crear la historia de éxito para la mascota con ID: {}",
                    idMascota,
                    e);

            throw new HistoriaExitoException(
                    String.format(ERROR_CREAR, idMascota),
                    e);
        }
    }

    // Método para actualizar una historia de éxito asociada a una mascota
    @Transactional(rollbackOn = Exception.class)
    public void actualizarHistoriaExito(
            HistoriaExitoEntity historiaExito,
            Long idMascota) {

        try {
            validarMascotaActiva(idMascota);

            HistoriaExitoEntity historiaExitoSubmit =
                    historiaExitoRepository.findByMascotaIdAndId(
                            idMascota,
                            historiaExito.getId());

            if (historiaExitoSubmit == null) {
                log.warn(
                        "La historia de éxito con ID: {} no existe",
                        historiaExito.getId());

                throw new HistoriaExitoException(
                        String.format(
                                HISTORIA_NO_EXISTE,
                                historiaExito.getId()));
            }

            log.info(
                    "La historia de éxito con ID asociada a la mascota seleccionada: {} existe",
                    historiaExito.getId());

            historiaExitoRepository.save(historiaExitoSubmit);

            log.info(
                    "Historia de éxito con ID: {} actualizada exitosamente para la mascota con ID: {}",
                    historiaExito.getId(),
                    idMascota);

        } catch (HistoriaExitoException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al actualizar la historia de éxito con ID: {} para la mascota con ID: {}",
                    historiaExito.getId(),
                    idMascota,
                    e);

            throw new HistoriaExitoException(
                    String.format(
                            ERROR_ACTUALIZAR,
                            historiaExito.getId(),
                            idMascota),
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public HistoriaExitoEntity obtenerHistoriaExitoByIdAndIdMascota(
            Long idHistoriaExito,
            Long idMascota) {

        try {
            validarMascotaActiva(idMascota);

            HistoriaExitoEntity historiaExito =
                    historiaExitoRepository.findByMascotaIdAndId(
                            idMascota,
                            idHistoriaExito);

            if (historiaExito != null) {
                log.info(
                        "La historia de éxito con ID asociada a la mascota seleccionada: {} existe",
                        idHistoriaExito);

                return historiaExito;
            }

            log.warn(
                    "La historia de éxito con ID: {} no existe",
                    idHistoriaExito);

            throw new HistoriaExitoException(
                    String.format(HISTORIA_NO_EXISTE, idHistoriaExito));

        } catch (HistoriaExitoException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al obtener la historia de éxito con ID: {} para la mascota con ID: {}",
                    idHistoriaExito,
                    idMascota,
                    e);

            throw new HistoriaExitoException(
                    String.format(
                            ERROR_OBTENER,
                            idHistoriaExito,
                            idMascota),
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public List<HistoriaExitoEntity> obtenerHistoriasExitoByIdMascota(
            Long idMascota) {

        try {
            validarMascotaActiva(idMascota);

            List<HistoriaExitoEntity> historiasExito =
                    historiaExitoRepository.findByMascotaId(idMascota);

            log.info(
                    "Se encontraron {} historias de éxito para la mascota con ID: {}",
                    historiasExito.size(),
                    idMascota);

            return historiasExito;

        } catch (HistoriaExitoException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al obtener las historias de éxito para la mascota con ID: {}",
                    idMascota,
                    e);

            throw new HistoriaExitoException(
                    String.format(ERROR_OBTENER_TODAS, idMascota),
                    e);
        }
    }

    /**
     * Valida que la mascota exista y se encuentre activa.
     *
     * @param idMascota identificador de la mascota
     * @return la mascota validada
     * @throws HistoriaExitoException si la mascota no existe o está inactiva
     */
    private MascotaEntity validarMascotaActiva(Long idMascota) {

        MascotaEntity mascota = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> {
                    log.warn(
                            "La mascota con ID: {} no existe",
                            idMascota);

                    return new HistoriaExitoException(
                            String.format(
                                    MASCOTA_NO_EXISTE,
                                    idMascota));
                });

        log.info(
                "La mascota con ID: {} existe",
                idMascota);

        if ("Inactivo".equals(mascota.getEstado())) {
            log.warn(
                    "La mascota con ID: {} se encuentra inactiva",
                    idMascota);

            throw new HistoriaExitoException(
                    String.format(
                            MASCOTA_INACTIVA,
                            idMascota));
        }

        log.info(
                "La mascota con ID: {} se encuentra activa",
                idMascota);

        return mascota;
    }

    /**
     * Excepción específica para errores relacionados con historias de éxito.
     */
    public static class HistoriaExitoException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public HistoriaExitoException(String message) {
            super(message);
        }

        public HistoriaExitoException(
                String message,
                Throwable cause) {
            super(message, cause);
        }
    }
}
