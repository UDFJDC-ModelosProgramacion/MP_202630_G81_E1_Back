package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.Vacuna;

public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
}
