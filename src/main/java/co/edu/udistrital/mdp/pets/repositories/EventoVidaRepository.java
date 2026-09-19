package co.edu.udistrital.mdp.pets.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.EventoVidaEntity;

public interface EventoVidaRepository extends JpaRepository<EventoVidaEntity, Long> {
    
    List<EventoVidaEntity> findByMascotaId(Long mascotaId);

    List<EventoVidaEntity> findByMascotaIdAndTipo(Long mascotaId, String tipo);

    List<EventoVidaEntity> findByMascotaIdAndTipoAndFechaBetween(Long mascotaId, String tipo, Date fechaInicio, Date fechaFin);

    List<EventoVidaEntity> findByMascotaIdAndTipoAndFechaAfter(Long mascotaId, String tipo, Date fechaInicio);

}
