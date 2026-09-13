package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.RefugioEntity;

public interface RefugioRepository extends JpaRepository<RefugioEntity, Long> {

    RefugioEntity findByNombre(String nombre);

    RefugioEntity findByCiudad(String ciudad);

}
