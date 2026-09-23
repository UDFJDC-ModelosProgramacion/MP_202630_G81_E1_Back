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
 * Pruebas de persistencia JPA para VeterinarioEntity, independientes de
 * VeterinarioService: solo validan que la entidad y sus relaciones
 * (@ManyToOne hacia Refugio, @OneToMany hacia Seguimiento) se mapean bien.
 */
@DataJpaTest
public class VeterinarioEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<VeterinarioEntity> data = new ArrayList<>();
    private RefugioEntity refugio;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SeguimientoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RefugioEntity").executeUpdate();
    }

    private void insertData() {
        refugio = factory.manufacturePojo(RefugioEntity.class);
        entityManager.persist(refugio);

        for (int i = 0; i < 3; i++) {
            VeterinarioEntity entity = factory.manufacturePojo(VeterinarioEntity.class);
            entity.setRefugio(refugio);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testPersistVeterinario() {
        VeterinarioEntity nuevo = factory.manufacturePojo(VeterinarioEntity.class);
        nuevo.setRefugio(refugio);
        entityManager.persist(nuevo);

        VeterinarioEntity encontrado = entityManager.find(VeterinarioEntity.class, nuevo.getId());

        assertNotNull(encontrado);
        assertEquals(nuevo.getNombre(), encontrado.getNombre());
        assertEquals(nuevo.getEspecialidad(), encontrado.getEspecialidad());
    }

    @Test
    void testFindVeterinario() {
        VeterinarioEntity existente = data.get(0);
        VeterinarioEntity encontrado = entityManager.find(VeterinarioEntity.class, existente.getId());

        assertNotNull(encontrado);
        assertEquals(existente.getNombre(), encontrado.getNombre());
    }

    @Test
    void testUpdateVeterinario() {
        VeterinarioEntity existente = data.get(0);
        existente.setEspecialidad("Cirugia");
        entityManager.merge(existente);

        VeterinarioEntity actualizado = entityManager.find(VeterinarioEntity.class, existente.getId());
        assertEquals("Cirugia", actualizado.getEspecialidad());
    }

    @Test
    void testDeleteVeterinario() {
        VeterinarioEntity existente = data.get(0);
        VeterinarioEntity veterinario = entityManager.find(VeterinarioEntity.class, existente.getId());
        entityManager.remove(veterinario);

        VeterinarioEntity eliminado = entityManager.find(VeterinarioEntity.class, existente.getId());
        assertNull(eliminado);
    }

    @Test
    void testRefugioRelation() {
        VeterinarioEntity existente = data.get(0);
        VeterinarioEntity encontrado = entityManager.find(VeterinarioEntity.class, existente.getId());

        assertNotNull(encontrado.getRefugio());
        assertEquals(refugio.getId(), encontrado.getRefugio().getId());
    }

    @Test
    void testSeguimientosRelation() {
        VeterinarioEntity veterinario = data.get(0);
        MascotaEntity mascota = factory.manufacturePojo(MascotaEntity.class);
        mascota.setRefugio(refugio);
        entityManager.persist(mascota);

        SeguimientoEntity seguimiento = factory.manufacturePojo(SeguimientoEntity.class);
        seguimiento.setMascota(mascota);
        seguimiento.setVeterinario(veterinario);
        entityManager.persist(seguimiento);
        veterinario.getSeguimientos().add(seguimiento);

        VeterinarioEntity actualizado = entityManager.find(VeterinarioEntity.class, veterinario.getId());
        assertEquals(1, actualizado.getSeguimientos().size());
        assertEquals(seguimiento.getId(), actualizado.getSeguimientos().get(0).getId());
    }
}
