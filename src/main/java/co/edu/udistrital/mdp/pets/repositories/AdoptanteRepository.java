package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.Adoptante;

public interface AdoptanteRepository extends JpaRepository<Adoptante, Long> {
}
