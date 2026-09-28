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
public class ActualizacionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<ActualizacionEntity> data = new ArrayList<>();
    private MascotaEntity mascota;
    private AdoptanteEntity adoptante;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ActualizacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        adoptante = factory.manufacturePojo(AdoptanteEntity.class);
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(adoptante);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            ActualizacionEntity entity = factory.manufacturePojo(ActualizacionEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateActualizacion() {
        ActualizacionEntity entity = factory.manufacturePojo(ActualizacionEntity.class);
        entity.setMascota(mascota);
        entity.setAdoptante(adoptante);
        ActualizacionEntity result = entityManager.persistFlushFind(entity);
        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
        assertEquals(entity.getArchivoUrl(), result.getArchivoUrl());
    }

    @Test
    void testGetActualizacion() {
        ActualizacionEntity entity = data.get(0);
        ActualizacionEntity result = entityManager.find(ActualizacionEntity.class, entity.getId());
        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
    }

    @Test
    void testUpdateActualizacion() {
        ActualizacionEntity entity = data.get(0);
        ActualizacionEntity newData = factory.manufacturePojo(ActualizacionEntity.class);
        entity.setTipo(newData.getTipo());
        entity.setDescripcion(newData.getDescripcion());
        entity.setArchivoUrl(newData.getArchivoUrl());
        entityManager.merge(entity);
        ActualizacionEntity result = entityManager.find(ActualizacionEntity.class, entity.getId());
        assertEquals(newData.getTipo(), result.getTipo());
        assertEquals(newData.getDescripcion(), result.getDescripcion());
        assertEquals(newData.getArchivoUrl(), result.getArchivoUrl());
    }

    @Test
    void testDeleteActualizacion() {
        ActualizacionEntity entity = data.get(0);
        entityManager.remove(entity);
        ActualizacionEntity deleted = entityManager.find(ActualizacionEntity.class, entity.getId());
        assertNull(deleted);
    }
}