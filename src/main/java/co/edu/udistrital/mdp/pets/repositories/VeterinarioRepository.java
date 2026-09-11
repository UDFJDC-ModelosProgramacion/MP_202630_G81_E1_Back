package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;

public interface VeterinarioRepository extends JpaRepository<VeterinarioEntity, Long> {

    List<VeterinarioEntity> findByEspecialidad(String especialidad);

    List<VeterinarioEntity> findByRefugioId(Long refugioId);

}
