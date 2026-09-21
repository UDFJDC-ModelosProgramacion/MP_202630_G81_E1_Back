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
import co.edu.udistrital.mdp.pets.entities.SeguimientoEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de negocio para MascotaService.
 * MascotaRepository y RefugioRepository llegan inyectados por Spring
 * (respaldados por H2) gracias a @DataJpaTest + @Import(MascotaService.class).
 */
@DataJpaTest
@Transactional
@Import(MascotaService.class)
public class MascotaServiceTest {

    @Autowired
    private MascotaService mascotaService;

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
            entity.setEdad(2);
            entity.setEstado("Disponible");
            entity.setRefugio(refugio);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    // ---------- createMascota ----------

    @Test
    void testCreateMascotaValida() throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity nueva = factory.manufacturePojo(MascotaEntity.class);
        nueva.setEdad(3);
        nueva.setEstado("Disponible");
        nueva.setRefugio(refugio);

        MascotaEntity result = mascotaService.createMascota(nueva);

        assertNotNull(result);
        assertEquals(refugio.getId(), result.getRefugio().getId());
    }

    @Test
    void testCreateMascotaNombreVacioFallaException() {
        MascotaEntity nueva = factory.manufacturePojo(MascotaEntity.class);
        nueva.setNombre("");
        nueva.setRefugio(refugio);

        assertThrows(IllegalOperationException.class, () -> mascotaService.createMascota(nueva));
    }

    @Test
    void testCreateMascotaEdadNegativaFallaException() {
        MascotaEntity nueva = factory.manufacturePojo(MascotaEntity.class);
        nueva.setEdad(-1);
        nueva.setRefugio(refugio);

        assertThrows(IllegalOperationException.class, () -> mascotaService.createMascota(nueva));
    }

    @Test
    void testCreateMascotaEstadoInvalidoFallaException() {
        MascotaEntity nueva = factory.manufacturePojo(MascotaEntity.class);
        nueva.setEdad(1);
        nueva.setEstado("Perdida");
        nueva.setRefugio(refugio);

        assertThrows(IllegalOperationException.class, () -> mascotaService.createMascota(nueva));
    }

    @Test
    void testCreateMascotaRefugioInexistenteFallaException() {
        MascotaEntity nueva = factory.manufacturePojo(MascotaEntity.class);
        nueva.setEdad(1);
        nueva.setEstado("Disponible");
        RefugioEntity refugioFalso = new RefugioEntity();
        refugioFalso.setId(0L);
        nueva.setRefugio(refugioFalso);

        assertThrows(EntityNotFoundException.class, () -> mascotaService.createMascota(nueva));
    }

    // ---------- getMascotas / getMascota ----------

    @Test
    void testGetMascotas() {
        assertEquals(data.size(), mascotaService.getMascotas().size());
    }

    @Test
    void testGetMascotaValida() throws EntityNotFoundException {
        MascotaEntity entity = data.get(0);
        MascotaEntity result = mascotaService.getMascota(entity.getId());
        assertEquals(entity.getNombre(), result.getNombre());
    }

    @Test
    void testGetMascotaInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> mascotaService.getMascota(0L));
    }

    // ---------- updateMascota ----------

    @Test
    void testUpdateMascotaValida() throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity entity = data.get(0);
        MascotaEntity cambios = factory.manufacturePojo(MascotaEntity.class);
        cambios.setEdad(4);
        cambios.setEstado("Adoptado");
        cambios.setRefugio(refugio);

        MascotaEntity result = mascotaService.updateMascota(entity.getId(), cambios);

        assertEquals("Adoptado", result.getEstado());
    }

    @Test
    void testUpdateMascotaEdadNegativaFallaException() {
        MascotaEntity entity = data.get(0);
        MascotaEntity cambios = factory.manufacturePojo(MascotaEntity.class);
        cambios.setEdad(-5);
        cambios.setRefugio(refugio);

        assertThrows(IllegalOperationException.class, () -> mascotaService.updateMascota(entity.getId(), cambios));
    }

    @Test
    void testUpdateMascotaInexistenteFallaException() {
        MascotaEntity cambios = factory.manufacturePojo(MascotaEntity.class);
        cambios.setEdad(1);
        cambios.setEstado("Disponible");
        cambios.setRefugio(refugio);

        assertThrows(EntityNotFoundException.class, () -> mascotaService.updateMascota(0L, cambios));
    }

    // ---------- deleteMascota ----------

    @Test
    void testDeleteMascotaValida() throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity entity = data.get(0);
        mascotaService.deleteMascota(entity.getId());

        MascotaEntity eliminada = entityManager.find(MascotaEntity.class, entity.getId());
        assertNull(eliminada);
    }

    @Test
    void testDeleteMascotaConSeguimientosFallaException() {
        MascotaEntity entity = data.get(0);
        SeguimientoEntity seguimiento = factory.manufacturePojo(SeguimientoEntity.class);
        seguimiento.setMascota(entity);
        seguimiento.setVeterinario(null);
        entityManager.persist(seguimiento);
        entity.getSeguimientos().add(seguimiento);

        assertThrows(IllegalOperationException.class, () -> mascotaService.deleteMascota(entity.getId()));
    }

    @Test
    void testDeleteMascotaInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> mascotaService.deleteMascota(0L));
    }
}
