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
public class ResenaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<ResenaEntity> data = new ArrayList<>();
    private MascotaEntity mascota;
    private AdoptanteEntity adoptante;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ResenaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        adoptante = factory.manufacturePojo(AdoptanteEntity.class);
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(adoptante);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            ResenaEntity entity = factory.manufacturePojo(ResenaEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateResena() {
        ResenaEntity entity = factory.manufacturePojo(ResenaEntity.class);
        entity.setMascota(mascota);
        entity.setAdoptante(adoptante);
        ResenaEntity result = entityManager.persistFlushFind(entity);
        assertNotNull(result);
        assertEquals(entity.getCalificacion(), result.getCalificacion());
        assertEquals(entity.getComentario(), result.getComentario());
    }

    @Test
    void testGetResena() {
        ResenaEntity entity = data.get(0);
        ResenaEntity result = entityManager.find(ResenaEntity.class, entity.getId());
        assertNotNull(result);
        assertEquals(entity.getCalificacion(), result.getCalificacion());
    }

    @Test
    void testUpdateResena() {
        ResenaEntity entity = data.get(0);
        ResenaEntity newData = factory.manufacturePojo(ResenaEntity.class);
        entity.setCalificacion(newData.getCalificacion());
        entity.setComentario(newData.getComentario());
        entityManager.merge(entity);
        ResenaEntity result = entityManager.find(ResenaEntity.class, entity.getId());
        assertEquals(newData.getCalificacion(), result.getCalificacion());
        assertEquals(newData.getComentario(), result.getComentario());
    }

    @Test
    void testDeleteResena() {
        ResenaEntity entity = data.get(0);
        entityManager.remove(entity);
        ResenaEntity deleted = entityManager.find(ResenaEntity.class, entity.getId());
        assertNull(deleted);
    }
}