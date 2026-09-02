package co.edu.udistrital.mdp.ZZZ.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.ZZZ.entities.MascotaEntity;

@Repository
public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {

    List<MascotaEntity> findByEspecie(String especie);

    List<MascotaEntity> findByEstado(String estado);

    List<MascotaEntity> findByRefugioId(Long refugioId);

    @Query("SELECT m FROM MascotaEntity m WHERE m.compatibleConNinos = true AND m.estado = :estado")
    List<MascotaEntity> findDisponiblesCompatiblesConNinos(@Param("estado") String estado);

}
