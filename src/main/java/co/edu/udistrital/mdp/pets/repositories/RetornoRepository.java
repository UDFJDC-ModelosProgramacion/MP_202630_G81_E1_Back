package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.RetornoEntity;

public interface RetornoRepository extends JpaRepository<RetornoEntity, Long> {

    List<RetornoEntity> findByCompatibleReAdopcion(Boolean compatibleReAdopcion);

    Optional<RetornoEntity> findByAdopcionId(Long adopcionId);

}
