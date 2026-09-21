package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopcionRepository;

@Service
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;

    public AdopcionService(AdopcionRepository adopcionRepository) {
        this.adopcionRepository = adopcionRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public AdopcionEntity createAdopcion(AdopcionEntity adopcion) {
        try {
            return adopcionRepository.save(adopcion);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear la adopción.", e);
        }
    }

    public List<AdopcionEntity> getAdopciones() {
        return adopcionRepository.findAll();
    }

    public AdopcionEntity getAdopcion(Long id) {
        return adopcionRepository.findById(id).orElse(null);
    }

    public List<AdopcionEntity> getAdopcionesByEstado(String estado) {
        return adopcionRepository.findByEstado(estado);
    }

    public AdopcionEntity getAdopcionBySolicitud(Long solicitudId) {
        return adopcionRepository.findBySolicitudId(solicitudId).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public AdopcionEntity updateAdopcion(Long id, AdopcionEntity adopcion) {
        try {
            AdopcionEntity entity = getAdopcion(id);

            if (entity == null) {
                throw new IllegalArgumentException("La adopción no existe.");
            }

            adopcion.setId(id);
            return adopcionRepository.save(adopcion);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar la adopción.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAdopcion(Long id) {
        try {
            adopcionRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar la adopción.", e);
        }
    }
}