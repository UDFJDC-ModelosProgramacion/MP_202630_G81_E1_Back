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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de persistencia JPA para RefugioEntity, independientes de
 * RefugioService: solo validan que la entidad y sus anotaciones
 * (@OneToMany hacia Mascota y Veterinario) se mapean y persisten bien.
 */
@DataJpaTest
public class RefugioEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<RefugioEntity> data = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RefugioEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            RefugioEntity entity = factory.manufacturePojo(RefugioEntity.class);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testPersistRefugio() {
        RefugioEntity nuevo = factory.manufacturePojo(RefugioEntity.class);
        entityManager.persist(nuevo);

        RefugioEntity encontrado = entityManager.find(RefugioEntity.class, nuevo.getId());

        assertNotNull(encontrado);
        assertEquals(nuevo.getNombre(), encontrado.getNombre());
        assertEquals(nuevo.getCiudad(), encontrado.getCiudad());
    }

    @Test
    void testFindRefugio() {
        RefugioEntity existente = data.get(0);
        RefugioEntity encontrado = entityManager.find(RefugioEntity.class, existente.getId());

        assertNotNull(encontrado);
        assertEquals(existente.getNombre(), encontrado.getNombre());
    }

    @Test
    void testUpdateRefugio() {
        RefugioEntity existente = data.get(0);
        existente.setNombre("Nombre Actualizado");
        entityManager.merge(existente);

        RefugioEntity actualizado = entityManager.find(RefugioEntity.class, existente.getId());
        assertEquals("Nombre Actualizado", actualizado.getNombre());
    }

    @Test
    void testDeleteRefugio() {
        RefugioEntity existente = data.get(0);
        RefugioEntity refugio = entityManager.find(RefugioEntity.class, existente.getId());
        entityManager.remove(refugio);

        RefugioEntity eliminado = entityManager.find(RefugioEntity.class, existente.getId());
        assertNull(eliminado);
    }

    @Test
    void testMascotasRelation() {
        RefugioEntity refugio = data.get(0);
        MascotaEntity mascota = factory.manufacturePojo(MascotaEntity.class);
        mascota.setRefugio(refugio);
        entityManager.persist(mascota);
        refugio.getMascotas().add(mascota);

        RefugioEntity actualizado = entityManager.find(RefugioEntity.class, refugio.getId());
        assertEquals(1, actualizado.getMascotas().size());
        assertEquals(mascota.getId(), actualizado.getMascotas().get(0).getId());
    }

    @Test
    void testVeterinariosRelation() {
        RefugioEntity refugio = data.get(0);
        VeterinarioEntity veterinario = factory.manufacturePojo(VeterinarioEntity.class);
        veterinario.setRefugio(refugio);
        entityManager.persist(veterinario);
        refugio.getVeterinarios().add(veterinario);

        RefugioEntity actualizado = entityManager.find(RefugioEntity.class, refugio.getId());
        assertEquals(1, actualizado.getVeterinarios().size());
        assertEquals(veterinario.getId(), actualizado.getVeterinarios().get(0).getId());
    }
}
