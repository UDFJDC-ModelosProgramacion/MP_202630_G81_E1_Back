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
public class HistoriaExitoEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<HistoriaExitoEntity> data = new ArrayList<>();

    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from HistoriaExitoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            HistoriaExitoEntity entity = factory.manufacturePojo(HistoriaExitoEntity.class);
            entity.setMascota(mascota);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateHistoriaExito() {
        HistoriaExitoEntity newEntity = factory.manufacturePojo(HistoriaExitoEntity.class);
        newEntity.setMascota(mascota);

        HistoriaExitoEntity result = entityManager.persistFlushFind(newEntity);

        assertNotNull(result);
        assertEquals(newEntity.getTitulo(), result.getTitulo());
        assertEquals(newEntity.getDescripcion(), result.getDescripcion());
        assertEquals(mascota.getId(), result.getMascota().getId());
    }

    @Test
    void testGetHistoriaExito() {
        HistoriaExitoEntity entity = data.get(0);
        HistoriaExitoEntity result = entityManager.find(HistoriaExitoEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getTitulo(), result.getTitulo());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testUpdateHistoriaExito() {
        HistoriaExitoEntity entity = data.get(0);
        HistoriaExitoEntity newData = factory.manufacturePojo(HistoriaExitoEntity.class);

        entity.setTitulo(newData.getTitulo());
        entity.setDescripcion(newData.getDescripcion());
        entityManager.merge(entity);

        HistoriaExitoEntity resp = entityManager.find(HistoriaExitoEntity.class, entity.getId());
        assertEquals(newData.getTitulo(), resp.getTitulo());
        assertEquals(newData.getDescripcion(), resp.getDescripcion());
    }

    @Test
    void testDeleteHistoriaExito() {
        HistoriaExitoEntity entity = data.get(0);
        entityManager.remove(entity);

        HistoriaExitoEntity deleted = entityManager.find(HistoriaExitoEntity.class, entity.getId());
        assertNull(deleted);
    }
}
