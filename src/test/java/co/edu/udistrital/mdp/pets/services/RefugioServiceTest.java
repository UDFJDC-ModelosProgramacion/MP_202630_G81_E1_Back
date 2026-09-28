package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de negocio para RefugioService.
 *
 * Se usa @DataJpaTest + @Import(RefugioService.class): Spring levanta una
 * base de datos H2 en memoria, crea el RefugioRepository real respaldado
 * por esa base, y se lo INYECTA por constructor a RefugioService. Es el
 * mismo mecanismo de inyeccion de dependencias explicado en la actividad,
 * aplicado ahora en las pruebas.
 */
@DataJpaTest
@Transactional
@Import(RefugioService.class)
public class RefugioServiceTest {

    @Autowired
    private RefugioService refugioService;

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

    // ---------- createRefugio ----------

    @Test
    void testCreateRefugioValido() throws IllegalOperationException {
        RefugioEntity nuevo = factory.manufacturePojo(RefugioEntity.class);
        nuevo.setNombre("Refugio Patitas Felices");

        RefugioEntity result = refugioService.createRefugio(nuevo);

        assertNotNull(result);
        RefugioEntity guardado = entityManager.find(RefugioEntity.class, result.getId());
        assertEquals(nuevo.getNombre(), guardado.getNombre());
    }

    @Test
    void testCreateRefugioNombreVacioFallaException() {
        RefugioEntity nuevo = factory.manufacturePojo(RefugioEntity.class);
        nuevo.setNombre(" ");

        assertThrows(IllegalOperationException.class, () -> refugioService.createRefugio(nuevo));
    }

    @Test
    void testCreateRefugioNombreDuplicadoFallaException() {
        RefugioEntity existente = data.get(0);
        RefugioEntity nuevo = factory.manufacturePojo(RefugioEntity.class);
        nuevo.setNombre(existente.getNombre());

        assertThrows(IllegalOperationException.class, () -> refugioService.createRefugio(nuevo));
    }

    // ---------- getRefugios / getRefugio ----------

    @Test
    void testGetRefugios() {
        List<RefugioEntity> lista = refugioService.getRefugios();
        assertEquals(data.size(), lista.size());
    }

    @Test
    void testGetRefugioValido() throws EntityNotFoundException {
        RefugioEntity entity = data.get(0);
        RefugioEntity result = refugioService.getRefugio(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getNombre(), result.getNombre());
    }

    @Test
    void testGetRefugioInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> refugioService.getRefugio(0L));
    }

    // ---------- updateRefugio ----------

    @Test
    void testUpdateRefugioValido() throws EntityNotFoundException, IllegalOperationException {
        RefugioEntity entity = data.get(0);
        RefugioEntity cambios = factory.manufacturePojo(RefugioEntity.class);
        cambios.setNombre("Nuevo Nombre Refugio");

        RefugioEntity result = refugioService.updateRefugio(entity.getId(), cambios);

        assertEquals("Nuevo Nombre Refugio", result.getNombre());
    }

    @Test
    void testUpdateRefugioNombreVacioFallaException() {
        RefugioEntity entity = data.get(0);
        RefugioEntity cambios = factory.manufacturePojo(RefugioEntity.class);
        cambios.setNombre("");

        assertThrows(IllegalOperationException.class, () -> refugioService.updateRefugio(entity.getId(), cambios));
    }

    @Test
    void testUpdateRefugioInexistenteFallaException() {
        RefugioEntity cambios = factory.manufacturePojo(RefugioEntity.class);

        assertThrows(EntityNotFoundException.class, () -> refugioService.updateRefugio(0L, cambios));
    }

    // ---------- deleteRefugio ----------

    @Test
    void testDeleteRefugioValido() throws EntityNotFoundException, IllegalOperationException {
        RefugioEntity entity = data.get(0);
        refugioService.deleteRefugio(entity.getId());

        RefugioEntity eliminado = entityManager.find(RefugioEntity.class, entity.getId());
        assertNull(eliminado);
    }

    @Test
    void testDeleteRefugioConMascotasFallaException() {
        RefugioEntity entity = data.get(0);
        MascotaEntity mascota = factory.manufacturePojo(MascotaEntity.class);
        mascota.setRefugio(entity);
        entityManager.persist(mascota);
        entity.getMascotas().add(mascota);

        assertThrows(IllegalOperationException.class, () -> refugioService.deleteRefugio(entity.getId()));
    }

    @Test
    void testDeleteRefugioInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> refugioService.deleteRefugio(0L));
    }
}
