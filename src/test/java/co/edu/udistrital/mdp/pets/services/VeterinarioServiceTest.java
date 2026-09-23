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

import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.entities.SeguimientoEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de negocio para VeterinarioService.
 * VeterinarioRepository y RefugioRepository llegan inyectados por Spring
 * (respaldados por H2) gracias a @DataJpaTest + @Import(VeterinarioService.class).
 */
@DataJpaTest
@Transactional
@Import(VeterinarioService.class)
public class VeterinarioServiceTest {

    @Autowired
    private VeterinarioService veterinarioService;

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

    // ---------- createVeterinario ----------

    @Test
    void testCreateVeterinarioValido() throws EntityNotFoundException, IllegalOperationException {
        VeterinarioEntity nuevo = factory.manufacturePojo(VeterinarioEntity.class);
        nuevo.setRefugio(refugio);

        VeterinarioEntity result = veterinarioService.createVeterinario(nuevo);

        assertNotNull(result);
        assertEquals(refugio.getId(), result.getRefugio().getId());
    }

    @Test
    void testCreateVeterinarioNombreVacioFallaException() {
        VeterinarioEntity nuevo = factory.manufacturePojo(VeterinarioEntity.class);
        nuevo.setNombre(" ");
        nuevo.setRefugio(refugio);

        assertThrows(IllegalOperationException.class, () -> veterinarioService.createVeterinario(nuevo));
    }

    @Test
    void testCreateVeterinarioRefugioInexistenteFallaException() {
        VeterinarioEntity nuevo = factory.manufacturePojo(VeterinarioEntity.class);
        RefugioEntity refugioFalso = new RefugioEntity();
        refugioFalso.setId(0L);
        nuevo.setRefugio(refugioFalso);

        assertThrows(EntityNotFoundException.class, () -> veterinarioService.createVeterinario(nuevo));
    }

    // ---------- getVeterinarios / getVeterinario ----------

    @Test
    void testGetVeterinarios() {
        assertEquals(data.size(), veterinarioService.getVeterinarios().size());
    }

    @Test
    void testGetVeterinarioValido() throws EntityNotFoundException {
        VeterinarioEntity entity = data.get(0);
        VeterinarioEntity result = veterinarioService.getVeterinario(entity.getId());
        assertEquals(entity.getNombre(), result.getNombre());
    }

    @Test
    void testGetVeterinarioInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> veterinarioService.getVeterinario(0L));
    }

    // ---------- updateVeterinario ----------

    @Test
    void testUpdateVeterinarioValido() throws EntityNotFoundException, IllegalOperationException {
        VeterinarioEntity entity = data.get(0);
        VeterinarioEntity cambios = factory.manufacturePojo(VeterinarioEntity.class);
        cambios.setEspecialidad("Cirugia");
        cambios.setRefugio(refugio);

        VeterinarioEntity result = veterinarioService.updateVeterinario(entity.getId(), cambios);

        assertEquals("Cirugia", result.getEspecialidad());
    }

    @Test
    void testUpdateVeterinarioEspecialidadVaciaFallaException() {
        VeterinarioEntity entity = data.get(0);
        VeterinarioEntity cambios = factory.manufacturePojo(VeterinarioEntity.class);
        cambios.setEspecialidad("");
        cambios.setRefugio(refugio);

        assertThrows(IllegalOperationException.class,
                () -> veterinarioService.updateVeterinario(entity.getId(), cambios));
    }

    @Test
    void testUpdateVeterinarioInexistenteFallaException() {
        VeterinarioEntity cambios = factory.manufacturePojo(VeterinarioEntity.class);
        cambios.setRefugio(refugio);

        assertThrows(EntityNotFoundException.class, () -> veterinarioService.updateVeterinario(0L, cambios));
    }

    // ---------- deleteVeterinario ----------

    @Test
    void testDeleteVeterinarioValido() throws EntityNotFoundException, IllegalOperationException {
        VeterinarioEntity entity = data.get(0);
        veterinarioService.deleteVeterinario(entity.getId());

        VeterinarioEntity eliminado = entityManager.find(VeterinarioEntity.class, entity.getId());
        assertNull(eliminado);
    }

    @Test
    void testDeleteVeterinarioConSeguimientosFallaException() {
        VeterinarioEntity entity = data.get(0);
        SeguimientoEntity seguimiento = factory.manufacturePojo(SeguimientoEntity.class);
        seguimiento.setVeterinario(entity);
        seguimiento.setMascota(null);
        entityManager.persist(seguimiento);
        entity.getSeguimientos().add(seguimiento);

        assertThrows(IllegalOperationException.class, () -> veterinarioService.deleteVeterinario(entity.getId()));
    }

    @Test
    void testDeleteVeterinarioInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> veterinarioService.deleteVeterinario(0L));
    }
}
