package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ActualizacionEntity;
import co.edu.udistrital.mdp.pets.repositories.ActualizacionRepository;

@Service
public class ActualizacionService {

    private final ActualizacionRepository actualizacionRepository;

    public ActualizacionService(ActualizacionRepository actualizacionRepository) {
        this.actualizacionRepository = actualizacionRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ActualizacionEntity createActualizacion(ActualizacionEntity actualizacion) {
        try {
            return actualizacionRepository.save(actualizacion);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear la actualización.", e);
        }
    }

    public List<ActualizacionEntity> getActualizaciones() {
        return actualizacionRepository.findAll();
    }

    public ActualizacionEntity getActualizacion(Long id) {
        return actualizacionRepository.findById(id).orElse(null);
    }

    public List<ActualizacionEntity> getActualizacionesByTipo(String tipo) {
        return actualizacionRepository.findByTipo(tipo);
    }

    public List<ActualizacionEntity> getActualizacionesByMascota(Long mascotaId) {
        return actualizacionRepository.findByMascotaId(mascotaId);
    }

    public List<ActualizacionEntity> getActualizacionesByAdoptante(Long adoptanteId) {
        return actualizacionRepository.findByAdoptanteId(adoptanteId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ActualizacionEntity updateActualizacion(Long id, ActualizacionEntity actualizacion) {
        try {
            ActualizacionEntity entity = getActualizacion(id);

            if (entity == null) {
                throw new IllegalArgumentException("La actualización no existe.");
            }
            actualizacion.setId(id);
            return actualizacionRepository.save(actualizacion);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar la actualización.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteActualizacion(Long id) {
        try {
            actualizacionRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar la actualización.", e);
        }
    }
}