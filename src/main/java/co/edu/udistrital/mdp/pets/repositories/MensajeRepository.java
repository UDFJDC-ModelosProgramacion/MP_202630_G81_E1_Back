package co.edu.udistrital.mdp.pets.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.MensajeEntity;

public interface MensajeRepository extends JpaRepository<MensajeEntity, Long> {
    
    List<MensajeEntity> findByAdoptanteId(Long adoptanteId);

    List<MensajeEntity> findByMascotaId(Long mascotaId);

    List<MensajeEntity> findByAdoptanteIdAndLeidoFalse(Long adoptanteId);

    List<MensajeEntity> findByAdoptanteAndDateBetween(Long adoptanteId ,Date fechaInicio, Date fechaFin);

    List<MensajeEntity> findByDateBetween(Long mascotaId ,Date fechaInicio, Date fechaFin);

}
