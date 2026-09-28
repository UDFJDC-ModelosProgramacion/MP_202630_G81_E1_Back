package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.FotografiaEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.repositories.FotografiaRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FotografiaService {

    private final FotografiaRepository fotografiaRepository;
    private final MascotaRepository mascotaRepository;

    FotografiaService(
            FotografiaRepository fotografiaRepository,
            MascotaRepository mascotaRepository) {
        this.fotografiaRepository = fotografiaRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public void eliminarFotografia(Long idFotografia, Long idMascota) {
        try {
            if (mascotaRepository.existsById(idMascota)) {
                log.info("La mascota con ID: {} existe", idMascota);
            } else {
                log.warn("La mascota con ID: {} no existe", idMascota);
                throw new FotografiaException(
                        "La mascota con ID: " + idMascota + " no existe");
            }

            log.info("Eliminando fotografía con ID: {}", idFotografia);
            fotografiaRepository.deleteById(idFotografia);

        } catch (FotografiaException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al eliminar la fotografía con ID: {}",
                    idFotografia,
                    e);

            throw new FotografiaException(
                    "Error al eliminar la fotografía con ID: "
                            + idFotografia,
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public Optional<FotografiaEntity> getFotografiasbyId(Long id) {
        try {
            if (fotografiaRepository.existsById(id)) {
                log.info("La fotografía con ID: {} existe", id);
            } else {
                log.warn("La fotografía con ID: {} no existe", id);
                throw new FotografiaException(
                        "La fotografía con ID: " + id + " no existe");
            }

            log.info("Listando todas las fotografías");
            return fotografiaRepository.findById(id);

        } catch (FotografiaException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Error al obtener la fotografía con ID: {}",
                    id,
                    e);

            throw new FotografiaException(
                    "Error al obtener la fotografía con ID: " + id,
                    e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void setFotografiaActive(
            Long idFotografia,
            MascotaEntity mascota) {

        try {
            log.info(
                    "Activando fotografía con ID: {}",
                    idFotografia);

            Optional<FotografiaEntity> fotografiaOpt =
                    fotografiaRepository.findById(idFotografia);

            if (fotografiaOpt.isPresent()) {
                FotografiaEntity fotografia = fotografiaOpt.get();
                fotografiaRepository.save(fotografia);
            } else {
                log.warn(
                        "Fotografía con ID: {} no encontrada",
                        idFotografia);
            }

        } catch (Exception e) {
            log.error(
                    "Error al activar la fotografía con ID: {}",
                    idFotografia,
                    e);

            throw new FotografiaException(
                    "Error al activar la fotografía con ID: "
                            + idFotografia,
                    e);
        }
    }

    /**
     * Excepción específica para errores relacionados con fotografías.
     */
    public static class FotografiaException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public FotografiaException(String message) {
            super(message);
        }

        public FotografiaException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
