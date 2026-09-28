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
public class MensajeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<MensajeEntity> data = new ArrayList<>();

    private MascotaEntity mascota;
    private AdoptanteEntity adoptante;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MensajeEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
    }

    private void insertData() {
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(mascota);

        adoptante = factory.manufacturePojo(AdoptanteEntity.class);
        entityManager.persist(adoptante);

        for (int i = 0; i < 3; i++) {
            MensajeEntity entity = factory.manufacturePojo(MensajeEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateMensaje() {
        MensajeEntity newEntity = factory.manufacturePojo(MensajeEntity.class);
        newEntity.setMascota(mascota);
        newEntity.setAdoptante(adoptante);

        MensajeEntity result = entityManager.persistFlushFind(newEntity);

        assertNotNull(result);
        assertEquals(newEntity.getAsunto(), result.getAsunto());
        assertEquals(newEntity.getContenido(), result.getContenido());
        assertEquals(newEntity.isLeido(), result.isLeido());
        assertEquals(mascota.getId(), result.getMascota().getId());
        assertEquals(adoptante.getId(), result.getAdoptante().getId());
    }

    @Test
    void testGetMensaje() {
        MensajeEntity entity = data.get(0);
        MensajeEntity result = entityManager.find(MensajeEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getAsunto(), result.getAsunto());
        assertEquals(entity.getContenido(), result.getContenido());
        assertEquals(entity.getMascota().getId(), result.getMascota().getId());
    }

    @Test
    void testUpdateMensaje() {
        MensajeEntity entity = data.get(0);
        MensajeEntity newData = factory.manufacturePojo(MensajeEntity.class);

        entity.setAsunto(newData.getAsunto());
        entity.setContenido(newData.getContenido());
        entity.setLeido(newData.isLeido());
        entityManager.merge(entity);

        MensajeEntity resp = entityManager.find(MensajeEntity.class, entity.getId());
        assertEquals(newData.getAsunto(), resp.getAsunto());
        assertEquals(newData.getContenido(), resp.getContenido());
        assertEquals(newData.isLeido(), resp.isLeido());
    }

    @Test
    void testDeleteMensaje() {
        MensajeEntity entity = data.get(0);
        entityManager.remove(entity);

        MensajeEntity deleted = entityManager.find(MensajeEntity.class, entity.getId());
        assertNull(deleted);
    }
}
