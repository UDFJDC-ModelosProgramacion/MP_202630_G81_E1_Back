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
 * Pruebas de persistencia JPA para MascotaEntity, independientes de
 * MascotaService: solo validan que la entidad y sus relaciones
 * (@ManyToOne hacia Refugio, @OneToMany hacia Seguimiento) se mapean bien.
 */
@DataJpaTest
public class MascotaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<MascotaEntity> data = new ArrayList<>();
    private RefugioEntity refugio;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from SeguimientoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from RefugioEntity").executeUpdate();
    }

    private void insertData() {
        refugio = factory.manufacturePojo(RefugioEntity.class);
        entityManager.persist(refugio);

        for (int i = 0; i < 3; i++) {
            MascotaEntity entity = factory.manufacturePojo(MascotaEntity.class);
            entity.setRefugio(refugio);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testPersistMascota() {
        MascotaEntity nueva = factory.manufacturePojo(MascotaEntity.class);
        nueva.setRefugio(refugio);
        entityManager.persist(nueva);

        MascotaEntity encontrada = entityManager.find(MascotaEntity.class, nueva.getId());

        assertNotNull(encontrada);
        assertEquals(nueva.getNombre(), encontrada.getNombre());
        assertEquals(nueva.getEspecie(), encontrada.getEspecie());
    }

    @Test
    void testFindMascota() {
        MascotaEntity existente = data.get(0);
        MascotaEntity encontrada = entityManager.find(MascotaEntity.class, existente.getId());

        assertNotNull(encontrada);
        assertEquals(existente.getNombre(), encontrada.getNombre());
    }

    @Test
    void testUpdateMascota() {
        MascotaEntity existente = data.get(0);
        existente.setEstado("Adoptado");
        entityManager.merge(existente);

        MascotaEntity actualizada = entityManager.find(MascotaEntity.class, existente.getId());
        assertEquals("Adoptado", actualizada.getEstado());
    }

    @Test
    void testDeleteMascota() {
        MascotaEntity existente = data.get(0);
        MascotaEntity mascota = entityManager.find(MascotaEntity.class, existente.getId());
        entityManager.remove(mascota);

        MascotaEntity eliminada = entityManager.find(MascotaEntity.class, existente.getId());
        assertNull(eliminada);
    }

    @Test
    void testRefugioRelation() {
        MascotaEntity existente = data.get(0);
        MascotaEntity encontrada = entityManager.find(MascotaEntity.class, existente.getId());

        assertNotNull(encontrada.getRefugio());
        assertEquals(refugio.getId(), encontrada.getRefugio().getId());
    }

    @Test
    void testSeguimientosRelation() {
        MascotaEntity mascota = data.get(0);
        VeterinarioEntity veterinario = factory.manufacturePojo(VeterinarioEntity.class);
        veterinario.setRefugio(refugio);
        entityManager.persist(veterinario);

        SeguimientoEntity seguimiento = factory.manufacturePojo(SeguimientoEntity.class);
        seguimiento.setMascota(mascota);
        seguimiento.setVeterinario(veterinario);
        entityManager.persist(seguimiento);
        mascota.getSeguimientos().add(seguimiento);

        MascotaEntity actualizada = entityManager.find(MascotaEntity.class, mascota.getId());
        assertEquals(1, actualizada.getSeguimientos().size());
        assertEquals(seguimiento.getId(), actualizada.getSeguimientos().get(0).getId());
    }
}
