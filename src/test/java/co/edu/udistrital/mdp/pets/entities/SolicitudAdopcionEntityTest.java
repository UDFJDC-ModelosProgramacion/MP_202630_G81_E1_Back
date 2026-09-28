package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
public class SolicitudAdopcionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<SolicitudAdopcionEntity> data = new ArrayList<>();

    private AdoptanteEntity adoptante;
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SolicitudAdopcionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {

        adoptante = factory.manufacturePojo(AdoptanteEntity.class);
        mascota = factory.manufacturePojo(MascotaEntity.class);

        entityManager.persist(adoptante);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            SolicitudAdopcionEntity entity = factory.manufacturePojo(SolicitudAdopcionEntity.class);
            entity.setAdoptante(adoptante);
            entity.setMascota(mascota);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateSolicitud() {
        SolicitudAdopcionEntity entity = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        entity.setAdoptante(adoptante);
        entity.setMascota(mascota);

        SolicitudAdopcionEntity result = entityManager.persistFlushFind(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
        assertEquals(entity.getTipoSolicitud(), result.getTipoSolicitud());
        assertEquals(entity.getObservacion(), result.getObservacion());
    }

    @Test
    void testGetSolicitud() {
        SolicitudAdopcionEntity entity = data.get(0);
        SolicitudAdopcionEntity result = entityManager.find(SolicitudAdopcionEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
        assertEquals(entity.getTipoSolicitud(), result.getTipoSolicitud());
    }

    @Test
    void testUpdateSolicitud() {
        SolicitudAdopcionEntity entity = data.get(0);
        SolicitudAdopcionEntity newData = factory.manufacturePojo(SolicitudAdopcionEntity.class);

        entity.setEstado(newData.getEstado());
        entity.setTipoSolicitud(newData.getTipoSolicitud());
        entity.setObservacion(newData.getObservacion());

        entityManager.merge(entity);

        SolicitudAdopcionEntity result = entityManager.find(SolicitudAdopcionEntity.class, entity.getId());

        assertEquals(newData.getEstado(), result.getEstado());
        assertEquals(newData.getTipoSolicitud(), result.getTipoSolicitud());
        assertEquals(newData.getObservacion(), result.getObservacion());
    }

    @Test
    void testDeleteSolicitud() {
        SolicitudAdopcionEntity entity = data.get(0);

        entityManager.remove(entity);

        SolicitudAdopcionEntity deleted = entityManager.find(SolicitudAdopcionEntity.class, entity.getId());

        assertNull(deleted);
    }
}