package co.edu.udistrital.mdp.ZZZ.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.ZZZ.entities.SeguimientoEntity;

@Repository
public interface SeguimientoRepository extends JpaRepository<SeguimientoEntity, Long> {

    List<SeguimientoEntity> findByMascotaId(Long mascotaId);

    List<SeguimientoEntity> findByVeterinarioId(Long veterinarioId);

    List<SeguimientoEntity> findByEstado(String estado);

}
