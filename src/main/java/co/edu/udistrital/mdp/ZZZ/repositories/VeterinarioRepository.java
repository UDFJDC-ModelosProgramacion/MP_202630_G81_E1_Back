package co.edu.udistrital.mdp.ZZZ.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.ZZZ.entities.VeterinarioEntity;

@Repository
public interface VeterinarioRepository extends JpaRepository<VeterinarioEntity, Long> {

    List<VeterinarioEntity> findByEspecialidad(String especialidad);

    List<VeterinarioEntity> findByRefugioId(Long refugioId);

}
