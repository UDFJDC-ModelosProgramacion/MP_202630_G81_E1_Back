package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.HistoriaExitoRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class HistoriaExitoService  {

    private final HistoriaExitoRepository historiaExitoRepository;
    private final MascotaRepository mascotaRepository;

    HistoriaExitoService(HistoriaExitoRepository historiaExitoRepository, MascotaRepository mascotaRepository, AdoptanteRepository adoptanteRepository) {
        this.historiaExitoRepository = historiaExitoRepository;
        this.mascotaRepository = mascotaRepository;
    }

    // Método para eliminar una historia de éxito asociada a una mascota
    @Transactional(rollbackOn = Exception.class)
    public void eliminarHistoriaExito(Long idHistoriaExito , Long idMascota) {
        try{
        // Verificar si la mascota existe
        if(mascotaRepository.existsById(idMascota)) {
            log.info("La mascota con ID: {} existe", idMascota);
        } else {
            log.warn("La mascota con ID: {} no existe", idMascota);
            throw new IllegalArgumentException("La mascota con ID: " + idMascota + " no existe");
        }

        // Verificar si la historia de éxito existe asociada a esta mascota
        if(historiaExitoRepository.findByMascotaIdAndId(idMascota, idHistoriaExito) != null) {
            log.info("La historia de éxito con ID asociada a la mascota seleccionada: {} existe", idHistoriaExito);
        } else {
            log.warn("La historia de éxito con ID: {} no existe", idHistoriaExito);
            throw new IllegalArgumentException("La historia de éxito con ID asociada a la mascota seleccionada: " + idHistoriaExito + " no existe");
        }

        // Verificar si la mascota se encuentra activa
        if(mascotaRepository.findById(idMascota).get().getEstado() != "Inactivo") {
            log.info("La mascota con ID: {} se encuentra activa", idMascota);
        } else {
            log.warn("La mascota con ID: {} se encuentra inactiva", idMascota);
            throw new IllegalArgumentException("La mascota con ID: " + idMascota + " se encuentra inactiva");
        }

        // Exito
        log.info("Eliminando historia de éxito con ID: {}", idHistoriaExito);
        historiaExitoRepository.deleteById(idHistoriaExito);
        }catch(Exception e) {
            log.error("Error al eliminar la historia de éxito con ID: {}", idHistoriaExito, e);
            throw new RuntimeException("Error al eliminar la historia de éxito con ID: " + idHistoriaExito, e);
        }
    }

    // Método para crear una historia de éxito asociada a una mascota
    @Transactional(rollbackOn = Exception.class)
    public void crearHistoriaExito(Long idMascota, String titulo, String descripcion) {
        try {
            // Verificar si la mascota existe
            if(mascotaRepository.existsById(idMascota)) {
                log.info("La mascota con ID: {} existe", idMascota);
            } else {
                log.warn("La mascota con ID: {} no existe", idMascota);
                throw new IllegalArgumentException("La mascota con ID: " + idMascota + " no existe");
            }

            // Verificar si la mascota se encuentra activa
            if(mascotaRepository.findById(idMascota).get().getEstado() != "Inactivo") {
                log.info("La mascota con ID: {} se encuentra activa", idMascota);
            } else {
                log.warn("La mascota con ID: {} se encuentra inactiva", idMascota);
                throw new IllegalArgumentException("La mascota con ID: " + idMascota + " se encuentra inactiva");
            }

            // Crear la historia de éxito
            HistoriaExitoEntity historiaExito = new HistoriaExitoEntity();
            historiaExito.setTitulo(titulo);
            historiaExito.setDescripcion(descripcion);
            historiaExito.setMascota(mascotaRepository.findById(idMascota).get());
            historiaExitoRepository.save(historiaExito);

            log.info("Historia de éxito creada exitosamente para la mascota con ID: {}", idMascota);
        } catch(Exception e) {
            log.error("Error al crear la historia de éxito para la mascota con ID: {}", idMascota, e);
            throw new RuntimeException("Error al crear la historia de éxito para la mascota con ID: " + idMascota, e);
        }
    }

    // Método para actualizar una historia de éxito asociada a una mascota
    @Transactional(rollbackOn = Exception.class)
    public void actualizarHistoriaExito(HistoriaExitoEntity historiaExito, Long idMascota) {
        try {
            // Verificar si la mascota existe
            if(mascotaRepository.existsById(idMascota)) {
                log.info("La mascota con ID: {} existe", idMascota);
            } else {
                log.warn("La mascota con ID: {} no existe", idMascota);
                throw new IllegalArgumentException("La mascota con ID: " + idMascota + " no existe");
            }

            // Verificar si la historia de éxito existe asociada a esta mascota
            HistoriaExitoEntity historiaExitoSubmit = historiaExitoRepository.findByMascotaIdAndId(idMascota, historiaExito.getId());
            log.info("La historia de éxito con ID asociada a la mascota seleccionada: {} existe", historiaExito.getId());

            // Actualizar la historia de éxito
            historiaExitoRepository.save(historiaExitoSubmit);

            log.info("Historia de éxito con ID: {} actualizada exitosamente para la mascota con ID: {}", historiaExito.getId(), idMascota);
        } catch(Exception e) {
            log.error("Error al actualizar la historia de éxito con ID: {} para la mascota con ID: {}", historiaExito.getId(), idMascota, e);
            throw new RuntimeException("Error al actualizar la historia de éxito con ID: " + historiaExito.getId() + " para la mascota con ID: " + idMascota, e);
        }
    }

    @Transactional (rollbackOn = Exception.class)
    public HistoriaExitoEntity obtenerHistoriaExitoByIdAndIdMascota(Long idHistoriaExito, Long idMascota) {
        try {
            // Verificar si la mascota existe
            if(mascotaRepository.existsById(idMascota)) {
                log.info("La mascota con ID: {} existe", idMascota);
            } else {
                log.warn("La mascota con ID: {} no existe", idMascota);
                throw new IllegalArgumentException("La mascota con ID: " + idMascota + " no existe");
            }

            // Verificar si la historia de éxito existe asociada a esta mascota
            HistoriaExitoEntity historiaExito = historiaExitoRepository.findByMascotaIdAndId(idMascota, idHistoriaExito);
            if(historiaExito != null) {
                log.info("La historia de éxito con ID asociada a la mascota seleccionada: {} existe", idHistoriaExito);
                return historiaExito;
            } else {
                log.warn("La historia de éxito con ID: {} no existe", idHistoriaExito);
                throw new IllegalArgumentException("La historia de éxito con ID asociada a la mascota seleccionada: " + idHistoriaExito + " no existe");
            }
        } catch(Exception e) {
            log.error("Error al obtener la historia de éxito con ID: {} para la mascota con ID: {}", idHistoriaExito, idMascota, e);
            throw new RuntimeException("Error al obtener la historia de éxito con ID: " + idHistoriaExito + " para la mascota con ID: " + idMascota, e);
        }
    }

    @Transactional (rollbackOn = Exception.class)
    public List<HistoriaExitoEntity> obtenerHistoriasExitoByIdMascota(Long idMascota) {
        try {
            // Verificar si la mascota existe
            if(mascotaRepository.existsById(idMascota)) {
                log.info("La mascota con ID: {} existe", idMascota);
            } else {
                log.warn("La mascota con ID: {} no existe", idMascota);
                throw new IllegalArgumentException("La mascota con ID: " + idMascota + " no existe");
            }

            // Obtener las historias de éxito asociadas a esta mascota
            List<HistoriaExitoEntity> historiasExito = historiaExitoRepository.findByMascotaId(idMascota);
            log.info("Se encontraron {} historias de éxito para la mascota con ID: {}", historiasExito.size(), idMascota);
            return historiasExito;
        } catch(Exception e) {
            log.error("Error al obtener las historias de éxito para la mascota con ID: {}", idMascota, e);
            throw new RuntimeException("Error al obtener las historias de éxito para la mascota con ID: " + idMascota, e);
        }
    }


}
