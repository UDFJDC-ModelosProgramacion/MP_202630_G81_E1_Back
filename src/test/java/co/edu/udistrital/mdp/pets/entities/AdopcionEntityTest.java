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
public class AdopcionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<AdopcionEntity> data = new ArrayList<>();

    private SolicitudAdopcionEntity solicitud;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdopcionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from SolicitudAdopcionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {

        AdoptanteEntity adoptante = factory.manufacturePojo(AdoptanteEntity.class);
        MascotaEntity mascota = factory.manufacturePojo(MascotaEntity.class);

        entityManager.persist(adoptante);
        entityManager.persist(mascota);

        solicitud = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);

        entityManager.persist(solicitud);

        for (int i = 0; i < 3; i++) {
            AdopcionEntity entity = factory.manufacturePojo(AdopcionEntity.class);
            entity.setSolicitud(solicitud);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateAdopcion() {
        AdopcionEntity entity = factory.manufacturePojo(AdopcionEntity.class);
        entity.setSolicitud(solicitud);

        AdopcionEntity result = entityManager.persistFlushFind(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
        assertEquals(entity.getObservacion(), result.getObservacion());
    }

    @Test
    void testGetAdopcion() {
        AdopcionEntity entity = data.get(0);
        AdopcionEntity result = entityManager.find(AdopcionEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testUpdateAdopcion() {
        AdopcionEntity entity = data.get(0);
        AdopcionEntity newData = factory.manufacturePojo(AdopcionEntity.class);

        entity.setEstado(newData.getEstado());
        entity.setObservacion(newData.getObservacion());

        entityManager.merge(entity);

        AdopcionEntity result = entityManager.find(AdopcionEntity.class, entity.getId());

        assertEquals(newData.getEstado(), result.getEstado());
        assertEquals(newData.getObservacion(), result.getObservacion());
    }

    @Test
    void testDeleteAdopcion() {
        AdopcionEntity entity = data.get(0);

        entityManager.remove(entity);

        AdopcionEntity deleted = entityManager.find(AdopcionEntity.class, entity.getId());

        assertNull(deleted);
    }
}