package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.ActualizacionEntity;

public interface ActualizacionRepository extends JpaRepository<ActualizacionEntity, Long> {
    List<ActualizacionEntity> findByTipo(String tipo);
    List<ActualizacionEntity> findByMascotaId(Long mascotaId);
    List<ActualizacionEntity> findByAdoptanteId(Long adoptanteId);
}
