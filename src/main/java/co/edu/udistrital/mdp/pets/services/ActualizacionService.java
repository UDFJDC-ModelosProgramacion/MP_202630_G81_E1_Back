package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ActualizacionEntity;
import co.edu.udistrital.mdp.pets.repositories.ActualizacionRepository;

@Service
public class ActualizacionService {

    private final ActualizacionRepository actualizacionRepository;

    ActualizacionService(ActualizacionRepository actualizacionRepository) {
        this.actualizacionRepository = actualizacionRepository;
    }

    public ActualizacionEntity createActualizacion(ActualizacionEntity actualizacion) {
        return actualizacionRepository.save(actualizacion);
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

    public ActualizacionEntity updateActualizacion(Long id, ActualizacionEntity actualizacion) {
        ActualizacionEntity actualizacionEntity = getActualizacion(id);
        if (actualizacionEntity == null) {
            return null;
        }
        actualizacion.setId(id);
        return actualizacionRepository.save(actualizacion);
    }

    public void deleteActualizacion(Long id) {
        actualizacionRepository.deleteById(id);
    }
}