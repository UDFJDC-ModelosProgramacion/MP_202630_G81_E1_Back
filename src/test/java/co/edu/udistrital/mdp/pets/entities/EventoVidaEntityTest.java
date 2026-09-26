package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@EntityScan(basePackageClasses = EventoVidaTestEntity.class)
@Transactional
public class EventoVidaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<EventoVidaTestEntity> data = new ArrayList<>();

    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EventoVidaTestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            EventoVidaTestEntity entity = factory.manufacturePojo(EventoVidaTestEntity.class);
            entity.setMascota(mascota);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateEventoVida() {
        EventoVidaTestEntity newEntity = factory.manufacturePojo(EventoVidaTestEntity.class);
        newEntity.setMascota(mascota);

        EventoVidaTestEntity result = entityManager.persistFlushFind(newEntity);

        assertNotNull(result);
        assertEquals(newEntity.getTipo(), result.getTipo());
        assertEquals(newEntity.getFecha().toLocalDate(), result.getFecha().toLocalDate());
        assertEquals(newEntity.getDescripcion(), result.getDescripcion());
        assertEquals(mascota.getId(), result.getMascota().getId());
    }

    @Test
    void testGetEventoVida() {
        EventoVidaTestEntity entity = data.get(0);
        EventoVidaTestEntity result = entityManager.find(EventoVidaTestEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
        assertEquals(entity.getFecha().toLocalDate(), result.getFecha().toLocalDate());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testUpdateEventoVida() {
        EventoVidaTestEntity entity = data.get(0);
        EventoVidaTestEntity newData = factory.manufacturePojo(EventoVidaTestEntity.class);

        entity.setTipo(newData.getTipo());
        entity.setFecha(newData.getFecha());
        entity.setDescripcion(newData.getDescripcion());
        entityManager.merge(entity);

        EventoVidaTestEntity result = entityManager.find(EventoVidaTestEntity.class, entity.getId());
        assertEquals(newData.getTipo(), result.getTipo());
        assertEquals(newData.getFecha().toLocalDate(), result.getFecha().toLocalDate());
        assertEquals(newData.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testDeleteEventoVida() {
        EventoVidaTestEntity entity = data.get(0);
        entityManager.remove(entity);

        EventoVidaTestEntity deleted = entityManager.find(EventoVidaTestEntity.class, entity.getId());
        assertNull(deleted);
    }
}

@jakarta.persistence.Entity
class EventoVidaTestEntity extends EventoVidaEntity {
}