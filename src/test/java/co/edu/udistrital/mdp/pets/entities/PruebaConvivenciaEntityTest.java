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
public class PruebaConvivenciaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<PruebaConvivenciaEntity> data = new ArrayList<>();

    private AdopcionEntity adopcion;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PruebaConvivenciaEntity").executeUpdate();
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
            PruebaConvivenciaEntity entity = factory.manufacturePojo(PruebaConvivenciaEntity.class);
            entity.setAdopcion(adopcion);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreatePruebaConvivencia() {
        PruebaConvivenciaEntity entity = factory.manufacturePojo(PruebaConvivenciaEntity.class);
        entity.setAdopcion(adopcion);

        PruebaConvivenciaEntity result = entityManager.persistFlushFind(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
        assertEquals(entity.getDuracionDias(), result.getDuracionDias());
        assertEquals(entity.getObservacion(), result.getObservacion());
    }

    @Test
    void testGetPruebaConvivencia() {
        PruebaConvivenciaEntity entity = data.get(0);
        PruebaConvivenciaEntity result = entityManager.find(PruebaConvivenciaEntity.class, entity.getId());

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testUpdatePruebaConvivencia() {
        PruebaConvivenciaEntity entity = data.get(0);
        PruebaConvivenciaEntity newData = factory.manufacturePojo(PruebaConvivenciaEntity.class);

        entity.setEstado(newData.getEstado());
        entity.setDuracionDias(newData.getDuracionDias());
        entity.setObservacion(newData.getObservacion());

        entityManager.merge(entity);

        PruebaConvivenciaEntity result = entityManager.find(PruebaConvivenciaEntity.class, entity.getId());

        assertEquals(newData.getEstado(), result.getEstado());
        assertEquals(newData.getDuracionDias(), result.getDuracionDias());
        assertEquals(newData.getObservacion(), result.getObservacion());
    }

    @Test
    void testDeletePruebaConvivencia() {
        PruebaConvivenciaEntity entity = data.get(0);

        entityManager.remove(entity);

        PruebaConvivenciaEntity deleted = entityManager.find(PruebaConvivenciaEntity.class, entity.getId());

        assertNull(deleted);
    }
}