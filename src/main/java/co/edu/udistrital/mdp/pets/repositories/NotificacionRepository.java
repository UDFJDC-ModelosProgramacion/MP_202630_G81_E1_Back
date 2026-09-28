package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.NotificacionEntity;

public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Long> {
    List<NotificacionEntity> findByCanal(String canal);
    List<NotificacionEntity> findByMascotaId(Long mascotaId);
    List<NotificacionEntity> findByAdoptanteId(Long adoptanteId);
}
