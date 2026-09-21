package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;

public interface AdopcionRepository extends JpaRepository<AdopcionEntity, Long> {

    List<AdopcionEntity> findByEstado(String estado);

    Optional<AdopcionEntity> findBySolicitudId(Long solicitudId);

}