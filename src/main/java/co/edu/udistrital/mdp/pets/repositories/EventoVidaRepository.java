package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.EventoVidaEntity;

public interface EventoVidaRepository extends JpaRepository<EventoVidaEntity, Long> {
    
    List<EventoVidaEntity> findByMascotaId(Long mascotaId);

    List<EventoVidaEntity> findByMascotaIdAndTipoEvento(Long mascotaId, String tipoEvento);

    List<EventoVidaEntity> findByMascotaIdAndTipoEventoAndFechaEventoBetween(Long mascotaId, String tipoEvento, String fechaInicio, String fechaFin);

    List<EventoVidaEntity> findByMascotaIdAndTipoEventoAndFechaEventoAfter(Long mascotaId, String tipoEvento, String fechaInicio);

}
