package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
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
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de negocio para SeguimientoService.
 * SeguimientoRepository, MascotaRepository y VeterinarioRepository llegan
 * inyectados por Spring (respaldados por H2) gracias a @DataJpaTest +
 * @Import(SeguimientoService.class).
 */
@DataJpaTest
@Transactional
@Import(SeguimientoService.class)
public class SeguimientoServiceTest {

    @Autowired
    private SeguimientoService seguimientoService;

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
            entity.setProximaCita(Date.valueOf("2026-02-10"));
            entity.setEstado("Programado");
            entity.setMascota(mascota);
            entity.setVeterinario(veterinario);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    // ---------- createSeguimiento ----------

    @Test
    void testCreateSeguimientoValido() throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity nuevo = factory.manufacturePojo(SeguimientoEntity.class);
        nuevo.setFechaAsignacion(Date.valueOf("2026-03-01"));
        nuevo.setProximaCita(Date.valueOf("2026-04-01"));
        nuevo.setEstado("Programado");
        nuevo.setMascota(mascota);
        nuevo.setVeterinario(veterinario);

        SeguimientoEntity result = seguimientoService.createSeguimiento(nuevo);

        assertNotNull(result);
        assertEquals(mascota.getId(), result.getMascota().getId());
    }

    @Test
    void testCreateSeguimientoFechaAsignacionNulaFallaException() {
        SeguimientoEntity nuevo = factory.manufacturePojo(SeguimientoEntity.class);
        nuevo.setFechaAsignacion(null);
        nuevo.setMascota(mascota);
        nuevo.setVeterinario(veterinario);

        assertThrows(IllegalOperationException.class, () -> seguimientoService.createSeguimiento(nuevo));
    }

    @Test
    void testCreateSeguimientoProximaCitaAntesDeFechaAsignacionFallaException() {
        SeguimientoEntity nuevo = factory.manufacturePojo(SeguimientoEntity.class);
        nuevo.setFechaAsignacion(Date.valueOf("2026-05-10"));
        nuevo.setProximaCita(Date.valueOf("2026-05-01")); // anterior a la fecha de asignacion
        nuevo.setMascota(mascota);
        nuevo.setVeterinario(veterinario);

        assertThrows(IllegalOperationException.class, () -> seguimientoService.createSeguimiento(nuevo));
    }

    @Test
    void testCreateSeguimientoMascotaInexistenteFallaException() {
        SeguimientoEntity nuevo = factory.manufacturePojo(SeguimientoEntity.class);
        nuevo.setFechaAsignacion(Date.valueOf("2026-03-01"));
        nuevo.setVeterinario(veterinario);
        MascotaEntity mascotaFalsa = new MascotaEntity();
        mascotaFalsa.setId(0L);
        nuevo.setMascota(mascotaFalsa);

        assertThrows(EntityNotFoundException.class, () -> seguimientoService.createSeguimiento(nuevo));
    }

    @Test
    void testCreateSeguimientoVeterinarioInexistenteFallaException() {
        SeguimientoEntity nuevo = factory.manufacturePojo(SeguimientoEntity.class);
        nuevo.setFechaAsignacion(Date.valueOf("2026-03-01"));
        nuevo.setMascota(mascota);
        VeterinarioEntity veterinarioFalso = new VeterinarioEntity();
        veterinarioFalso.setId(0L);
        nuevo.setVeterinario(veterinarioFalso);

        assertThrows(EntityNotFoundException.class, () -> seguimientoService.createSeguimiento(nuevo));
    }

    // ---------- getSeguimientos / getSeguimiento ----------

    @Test
    void testGetSeguimientos() {
        assertEquals(data.size(), seguimientoService.getSeguimientos().size());
    }

    @Test
    void testGetSeguimientoValido() throws EntityNotFoundException {
        SeguimientoEntity entity = data.get(0);
        SeguimientoEntity result = seguimientoService.getSeguimiento(entity.getId());
        assertEquals(entity.getObservacion(), result.getObservacion());
    }

    @Test
    void testGetSeguimientoInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> seguimientoService.getSeguimiento(0L));
    }

    // ---------- updateSeguimiento ----------

    @Test
    void testUpdateSeguimientoValido() throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity entity = data.get(0);
        SeguimientoEntity cambios = factory.manufacturePojo(SeguimientoEntity.class);
        cambios.setFechaAsignacion(Date.valueOf("2026-06-01"));
        cambios.setProximaCita(Date.valueOf("2026-07-01"));
        cambios.setEstado("Programado");
        cambios.setMascota(mascota);
        cambios.setVeterinario(veterinario);

        SeguimientoEntity result = seguimientoService.updateSeguimiento(entity.getId(), cambios);

        assertEquals(Date.valueOf("2026-06-01"), result.getFechaAsignacion());
    }

    @Test
    void testUpdateSeguimientoCompletadoFallaException() {
        SeguimientoEntity entity = data.get(0);
        entity.setEstado("Completado");
        entityManager.merge(entity);

        SeguimientoEntity cambios = factory.manufacturePojo(SeguimientoEntity.class);
        cambios.setFechaAsignacion(Date.valueOf("2026-06-01"));
        cambios.setMascota(mascota);
        cambios.setVeterinario(veterinario);

        assertThrows(IllegalOperationException.class,
                () -> seguimientoService.updateSeguimiento(entity.getId(), cambios));
    }

    @Test
    void testUpdateSeguimientoInexistenteFallaException() {
        SeguimientoEntity cambios = factory.manufacturePojo(SeguimientoEntity.class);
        cambios.setFechaAsignacion(Date.valueOf("2026-06-01"));
        cambios.setMascota(mascota);
        cambios.setVeterinario(veterinario);

        assertThrows(EntityNotFoundException.class, () -> seguimientoService.updateSeguimiento(0L, cambios));
    }

    // ---------- deleteSeguimiento ----------

    @Test
    void testDeleteSeguimientoValido() throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity entity = data.get(0);
        seguimientoService.deleteSeguimiento(entity.getId());

        SeguimientoEntity eliminado = entityManager.find(SeguimientoEntity.class, entity.getId());
        assertNull(eliminado);
    }

    @Test
    void testDeleteSeguimientoCompletadoFallaException() {
        SeguimientoEntity entity = data.get(0);
        entity.setEstado("Completado");
        entityManager.merge(entity);

        assertThrows(IllegalOperationException.class, () -> seguimientoService.deleteSeguimiento(entity.getId()));
    }

    @Test
    void testDeleteSeguimientoInexistenteFallaException() {
        assertThrows(EntityNotFoundException.class, () -> seguimientoService.deleteSeguimiento(0L));
    }
}
