package co.edu.udistrital.mdp.pets.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.VacunaEntity;

public interface VacunaRepository extends JpaRepository<VacunaEntity, Long> {

	Optional<VacunaEntity> findByNombreIgnoreCase(String nombre);
}