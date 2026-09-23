package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.EventoMedicoEntity;

public interface EventoMedicoRepository extends JpaRepository<EventoMedicoEntity, Long> {
    List<EventoMedicoEntity> findByMascotaId(Long mascotaId);
    List<EventoMedicoEntity> findByDiagnostico(String diagnostico);
}