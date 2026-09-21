package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.ResenaEntity;

public interface ResenaRepository extends JpaRepository<ResenaEntity, Long> {
    List<ResenaEntity> findByMascotaId(Long mascotaId);
    List<ResenaEntity> findByAdoptanteId(Long adoptanteId);
    List<ResenaEntity> findByCalificacion(Integer calificacion);
}