package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de persistencia JPA para SeguimientoEntity, independientes de
 * SeguimientoService: solo validan que la entidad y sus relaciones
 * (@ManyToOne hacia Mascota y Veterinario) se mapean bien.
 */
@DataJpaTest
public class SeguimientoEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<SeguimientoEntity> data = new ArrayList<>();
    private MascotaEntity mascota;
    private VeterinarioEntity veterinario;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SeguimientoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RefugioEntity").executeUpdate();
    }

    private void insertData() {
        RefugioEntity refugio = factory.manufacturePojo(RefugioEntity.class);
        entityManager.persist(refugio);

        mascota = factory.manufacturePojo(MascotaEntity.class);
        mascota.setRefugio(refugio);
        entityManager.persist(mascota);

        veterinario = factory.manufacturePojo(VeterinarioEntity.class);
        veterinario.setRefugio(refugio);
        entityManager.persist(veterinario);

        for (int i = 0; i < 3; i++) {
            SeguimientoEntity entity = factory.manufacturePojo(SeguimientoEntity.class);
            entity.setFechaAsignacion(Date.valueOf("2026-01-10"));
            entity.setMascota(mascota);
            entity.setVeterinario(veterinario);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testPersistSeguimiento() {
        SeguimientoEntity nuevo = factory.manufacturePojo(SeguimientoEntity.class);
        nuevo.setFechaAsignacion(Date.valueOf("2026-03-01"));
        nuevo.setMascota(mascota);
        nuevo.setVeterinario(veterinario);
        entityManager.persist(nuevo);

        SeguimientoEntity encontrado = entityManager.find(SeguimientoEntity.class, nuevo.getId());

        assertNotNull(encontrado);
        assertEquals(nuevo.getObservacion(), encontrado.getObservacion());
    }

    @Test
    void testFindSeguimiento() {
        SeguimientoEntity existente = data.get(0);
        SeguimientoEntity encontrado = entityManager.find(SeguimientoEntity.class, existente.getId());

        assertNotNull(encontrado);
        assertEquals(existente.getObservacion(), encontrado.getObservacion());
    }

    @Test
    void testUpdateSeguimiento() {
        SeguimientoEntity existente = data.get(0);
        existente.setEstado("Completado");
        entityManager.merge(existente);

        SeguimientoEntity actualizado = entityManager.find(SeguimientoEntity.class, existente.getId());
        assertEquals("Completado", actualizado.getEstado());
    }

    @Test
    void testDeleteSeguimiento() {
        SeguimientoEntity existente = data.get(0);
        SeguimientoEntity seguimiento = entityManager.find(SeguimientoEntity.class, existente.getId());
        entityManager.remove(seguimiento);

        SeguimientoEntity eliminado = entityManager.find(SeguimientoEntity.class, existente.getId());
        assertNull(eliminado);
    }

    @Test
    void testMascotaRelation() {
        SeguimientoEntity existente = data.get(0);
        SeguimientoEntity encontrado = entityManager.find(SeguimientoEntity.class, existente.getId());

        assertNotNull(encontrado.getMascota());
        assertEquals(mascota.getId(), encontrado.getMascota().getId());
    }

    @Test
    void testVeterinarioRelation() {
        SeguimientoEntity existente = data.get(0);
        SeguimientoEntity encontrado = entityManager.find(SeguimientoEntity.class, existente.getId());

        assertNotNull(encontrado.getVeterinario());
        assertEquals(veterinario.getId(), encontrado.getVeterinario().getId());
    }
}
