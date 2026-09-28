package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;

public interface AdoptanteRepository extends JpaRepository<AdoptanteEntity, Long> {

	Optional<AdoptanteEntity> findByEmailIgnoreCase(String email);

	List<AdoptanteEntity> findByCiudad(String ciudad);
}