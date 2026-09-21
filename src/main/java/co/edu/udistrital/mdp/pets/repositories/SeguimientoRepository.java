package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.SeguimientoEntity;

public interface SeguimientoRepository extends JpaRepository<SeguimientoEntity, Long> {

    List<SeguimientoEntity> findByMascotaId(Long mascotaId);

    List<SeguimientoEntity> findByVeterinarioId(Long veterinarioId);

    List<SeguimientoEntity> findByEstado(String estado);

}
