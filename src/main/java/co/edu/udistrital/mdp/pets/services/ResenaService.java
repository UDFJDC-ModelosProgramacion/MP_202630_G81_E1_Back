package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ResenaEntity;
import co.edu.udistrital.mdp.pets.repositories.ResenaRepository;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    public ResenaEntity createResena(ResenaEntity resena) {
        return resenaRepository.save(resena);
    }

    public List<ResenaEntity> getResenas() {
        return resenaRepository.findAll();
    }

    public ResenaEntity getResena(Long id) {
        return resenaRepository.findById(id).orElse(null);
    }

    public List<ResenaEntity> getResenasByMascota(Long mascotaId) {
        return resenaRepository.findByMascotaId(mascotaId);
    }

    public List<ResenaEntity> getResenasByAdoptante(Long adoptanteId) {
        return resenaRepository.findByAdoptanteId(adoptanteId);
    }

    public List<ResenaEntity> getResenasByCalificacion(Integer calificacion) {
        return resenaRepository.findByCalificacion(calificacion);
    }

    public ResenaEntity updateResena(Long id, ResenaEntity resena) {
        ResenaEntity resenaEntity = getResena(id);
        if (resenaEntity == null) {
            return null;
        }
        resena.setId(id);
        return resenaRepository.save(resena);
    }

    public void deleteResena(Long id) {
        resenaRepository.deleteById(id);
    }
}