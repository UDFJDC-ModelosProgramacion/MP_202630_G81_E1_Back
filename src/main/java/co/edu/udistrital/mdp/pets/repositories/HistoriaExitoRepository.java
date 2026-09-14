package co.edu.udistrital.mdp.pets.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;

public interface HistoriaExitoRepository extends JpaRepository<HistoriaExitoEntity, Long> {
    
    List<HistoriaExitoEntity> findByNombreContainingIgnoreCase(String nombre);

    List<HistoriaExitoEntity> findByDescripcionContainingIgnoreCase(String descripcion);

    List<HistoriaExitoEntity> findByFechaContainingIgnoreCase(Date fecha);

    List<HistoriaExitoEntity> findByMascotaId(Long mascotaId);
}
