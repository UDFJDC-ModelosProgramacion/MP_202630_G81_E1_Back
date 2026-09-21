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
public class RetornoEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<RetornoEntity> data = new ArrayList<>();

    private AdopcionEntity adopcion;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from RetornoEntity").executeUpdate();
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

        SolicitudAdopcionEntity solicitud = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);
        entityManager.persist(solicitud);

        adopcion = factory.manufacturePojo(AdopcionEntity.class);
        adopcion.setSolicitud(solicitud);
        entityManager.persist(adopcion);

        for (int i = 0; i < 3; i++) {
            RetornoEntity entity = factory.manufacturePojo(RetornoEntity.class);
            entity.setAdopcion(adopcion);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateRetorno() {
        RetornoEntity entity = factory.manufacturePojo(RetornoEntity.class);
        entity.setAdopcion(adopcion);

        RetornoEntity result = entityManager.persistFlushFind(entity);

        assertNotNull(result);
        assertEquals(entity.getMotivo(), result.getMotivo());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
        assertEquals(entity.getCompatibleReAdopcion(), result.getCompatibleReAdopcion());
    }

    @Test
    void testGetRetorno() {
        RetornoEntity entity = data.get(0);
        RetornoEntity result = entityManager.find(RetornoEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getMotivo(), result.getMotivo());
    }

    @Test
    void testUpdateRetorno() {
        RetornoEntity entity = data.get(0);
        RetornoEntity newData = factory.manufacturePojo(RetornoEntity.class);

        entity.setMotivo(newData.getMotivo());
        entity.setDescripcion(newData.getDescripcion());
        entity.setCompatibleReAdopcion(newData.getCompatibleReAdopcion());

        entityManager.merge(entity);

        RetornoEntity result = entityManager.find(RetornoEntity.class, entity.getId());

        assertEquals(newData.getMotivo(), result.getMotivo());
        assertEquals(newData.getDescripcion(), result.getDescripcion());
        assertEquals(newData.getCompatibleReAdopcion(), result.getCompatibleReAdopcion());
    }

    @Test
    void testDeleteRetorno() {
        RetornoEntity entity = data.get(0);

        entityManager.remove(entity);

        RetornoEntity deleted = entityManager.find(RetornoEntity.class, entity.getId());

        assertNull(deleted);
    }
}