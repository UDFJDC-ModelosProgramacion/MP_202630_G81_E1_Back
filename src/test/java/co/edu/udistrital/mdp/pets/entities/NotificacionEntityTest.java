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
public class NotificacionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<NotificacionEntity> data = new ArrayList<>();
    private MascotaEntity mascota;
    private AdoptanteEntity adoptante;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from NotificacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        adoptante = factory.manufacturePojo(AdoptanteEntity.class);
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(adoptante);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            NotificacionEntity entity = factory.manufacturePojo(NotificacionEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateNotificacion() {
        NotificacionEntity entity = factory.manufacturePojo(NotificacionEntity.class);
        entity.setMascota(mascota);
        entity.setAdoptante(adoptante);
        NotificacionEntity result = entityManager.persistFlushFind(entity);
        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
        assertEquals(entity.getMensaje(), result.getMensaje());
        assertEquals(entity.getCanal(), result.getCanal());
    }

    @Test
    void testGetNotificacion() {
        NotificacionEntity entity = data.get(0);
        NotificacionEntity result = entityManager.find(NotificacionEntity.class, entity.getId());
        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
    }

    @Test
    void testUpdateNotificacion() {
        NotificacionEntity entity = data.get(0);
        NotificacionEntity newData = factory.manufacturePojo(NotificacionEntity.class);
        entity.setTipo(newData.getTipo());
        entity.setMensaje(newData.getMensaje());
        entity.setCanal(newData.getCanal());
        entityManager.merge(entity);
        NotificacionEntity result = entityManager.find(NotificacionEntity.class, entity.getId());
        assertEquals(newData.getTipo(), result.getTipo());
        assertEquals(newData.getMensaje(), result.getMensaje());
        assertEquals(newData.getCanal(), result.getCanal());
    }

    @Test
    void testDeleteNotificacion() {
        NotificacionEntity entity = data.get(0);
        entityManager.remove(entity);
        NotificacionEntity deleted = entityManager.find(NotificacionEntity.class, entity.getId());
        assertNull(deleted);
    }
}