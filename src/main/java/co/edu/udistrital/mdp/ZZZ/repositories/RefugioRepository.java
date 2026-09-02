package co.edu.udistrital.mdp.ZZZ.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.ZZZ.entities.RefugioEntity;

@Repository
public interface RefugioRepository extends JpaRepository<RefugioEntity, Long> {

    RefugioEntity findByNombre(String nombre);

    RefugioEntity findByCiudad(String ciudad);

}
