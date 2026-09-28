package co.edu.udistrital.mdp.pets.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.RegistroVacunacionEntity;

public interface RegistroVacunacionRepository extends JpaRepository<RegistroVacunacionEntity, Long> {

	List<RegistroVacunacionEntity> findByMascotaId(Long mascotaId);

	List<RegistroVacunacionEntity> findByVacunaId(Long vacunaId);

	List<RegistroVacunacionEntity> findByProximaFechaBefore(Date fecha);
}