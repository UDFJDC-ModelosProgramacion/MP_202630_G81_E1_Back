package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ResenaEntity;
import co.edu.udistrital.mdp.pets.repositories.ResenaRepository;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResenaEntity createResena(ResenaEntity resena) {
        try {
            return resenaRepository.save(resena);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear la reseña.", e);
        }
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

    @Transactional(rollbackFor = Exception.class)
    public ResenaEntity updateResena(Long id, ResenaEntity resena) {
        try {
            ResenaEntity entity = getResena(id);

            if (entity == null) {
                throw new IllegalArgumentException("La reseña no existe.");
            }
            resena.setId(id);
            return resenaRepository.save(resena);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar la reseña.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteResena(Long id) {
        try {
            resenaRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar la reseña.", e);
        }
    }
}