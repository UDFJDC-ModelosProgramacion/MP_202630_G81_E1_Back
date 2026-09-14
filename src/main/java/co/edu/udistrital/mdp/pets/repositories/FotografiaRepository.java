package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.FotografiaEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad de fotografias
 */
public interface FotografiaRepository extends JpaRepository<FotografiaEntity, Long> {

    List<FotografiaEntity> findAllByMascotaId(Long mascotaId);

    List<FotografiaEntity> findByMascotaIdAndPrincipalTrue(Long mascotaId);


}
