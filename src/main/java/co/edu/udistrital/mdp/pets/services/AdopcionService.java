package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopcionRepository;

@Service
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;

    AdopcionService(AdopcionRepository adopcionRepository) {
        this.adopcionRepository = adopcionRepository;
    }

    public AdopcionEntity createAdopcion(AdopcionEntity adopcion) {
        return adopcionRepository.save(adopcion);
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

    public AdopcionEntity updateAdopcion(Long id, AdopcionEntity adopcion) {
        AdopcionEntity adopcionEntity = getAdopcion(id);

        if (adopcionEntity == null) {
            return null;
        }

        adopcion.setId(id);
        return adopcionRepository.save(adopcion);
    }

    public void deleteAdopcion(Long id) {
        adopcionRepository.deleteById(id);
    }
}