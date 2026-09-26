package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcionEntity, Long> {

    List<SolicitudAdopcionEntity> findByEstado(String estado);

    List<SolicitudAdopcionEntity> findByAdoptanteId(Long adoptanteId);

    List<SolicitudAdopcionEntity> findByMascotaId(Long mascotaId);

}