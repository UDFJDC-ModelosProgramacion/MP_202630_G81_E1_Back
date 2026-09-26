package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.PruebaConvivenciaEntity;

public interface PruebaConvivenciaRepository extends JpaRepository<PruebaConvivenciaEntity, Long> {

    List<PruebaConvivenciaEntity> findByEstado(String estado);

    Optional<PruebaConvivenciaEntity> findByAdopcionId(Long adopcionId);

}