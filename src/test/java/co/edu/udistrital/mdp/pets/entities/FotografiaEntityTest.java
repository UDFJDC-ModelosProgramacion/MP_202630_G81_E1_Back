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
@EntityScan(basePackageClasses = FotografiaTestEntity.class)
@Transactional
public class FotografiaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<FotografiaTestEntity> data = new ArrayList<>();

    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from FotografiaTestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            FotografiaTestEntity entity = factory.manufacturePojo(FotografiaTestEntity.class);
            entity.setMascota(mascota);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateFotografia() {
        FotografiaTestEntity newEntity = factory.manufacturePojo(FotografiaTestEntity.class);
        newEntity.setMascota(mascota);

        FotografiaTestEntity result = entityManager.persistFlushFind(newEntity);

        assertNotNull(result);
        assertEquals(newEntity.getUrl(), result.getUrl());
        assertEquals(newEntity.isPrincipal(), result.isPrincipal());
        assertEquals(newEntity.getDescripcion(), result.getDescripcion());
        assertEquals(mascota.getId(), result.getMascota().getId());
    }

    @Test
    void testGetFotografia() {
        FotografiaTestEntity entity = data.get(0);
        FotografiaTestEntity result = entityManager.find(FotografiaTestEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getUrl(), result.getUrl());
        assertEquals(entity.isPrincipal(), result.isPrincipal());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testUpdateFotografia() {
        FotografiaTestEntity entity = data.get(0);
        FotografiaTestEntity newData = factory.manufacturePojo(FotografiaTestEntity.class);

        entity.setUrl(newData.getUrl());
        entity.setPrincipal(newData.isPrincipal());
        entity.setDescripcion(newData.getDescripcion());
        entityManager.merge(entity);

        FotografiaTestEntity result = entityManager.find(FotografiaTestEntity.class, entity.getId());
        assertEquals(newData.getUrl(), result.getUrl());
        assertEquals(newData.isPrincipal(), result.isPrincipal());
        assertEquals(newData.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testDeleteFotografia() {
        FotografiaTestEntity entity = data.get(0);
        entityManager.remove(entity);

        FotografiaTestEntity deleted = entityManager.find(FotografiaTestEntity.class, entity.getId());
        assertNull(deleted);
    }
}

@jakarta.persistence.Entity
class FotografiaTestEntity extends FotografiaEntity {
}