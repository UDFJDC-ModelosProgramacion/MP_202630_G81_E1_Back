package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import co.edu.udistrital.mdp.pets.entities.RegistroVacunacionEntity;
import co.edu.udistrital.mdp.pets.entities.VacunaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de la lógica de la clase VacunaService.
 */
@DataJpaTest
@Transactional
@Import(VacunaService.class)
class VacunaServiceTest {

	@Autowired
	private VacunaService vacunaService;

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<VacunaEntity> vacunaList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from RegistroVacunacionEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VacunaEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			VacunaEntity vacuna = factory.manufacturePojo(VacunaEntity.class);
			entityManager.persist(vacuna);
			vacunaList.add(vacuna);
		}
	}

	// ---------- createVacuna ----------

	@Test
	void testCreateVacuna() throws IllegalOperationException {
		VacunaEntity nueva = factory.manufacturePojo(VacunaEntity.class);
		nueva.setNombre("Rabia " + System.nanoTime());

		VacunaEntity creada = vacunaService.createVacuna(nueva);

		assertEquals(nueva.getNombre(), creada.getNombre());
		VacunaEntity encontrada = entityManager.find(VacunaEntity.class, creada.getId());
		assertEquals(nueva.getNombre(), encontrada.getNombre());
	}

	@Test
	void testCreateVacunaConNombreVacio() {
		VacunaEntity nueva = factory.manufacturePojo(VacunaEntity.class);
		nueva.setNombre("");

		assertThrows(IllegalOperationException.class, () -> vacunaService.createVacuna(nueva));
	}

	@Test
	void testCreateVacunaConNombreDuplicado() {
		VacunaEntity nueva = factory.manufacturePojo(VacunaEntity.class);
		nueva.setNombre(vacunaList.get(0).getNombre());

		assertThrows(IllegalOperationException.class, () -> vacunaService.createVacuna(nueva));
	}

	// ---------- getVacunas ----------

	@Test
	void testGetVacunas() {
		List<VacunaEntity> lista = vacunaService.getVacunas();
		assertEquals(vacunaList.size(), lista.size());
	}

	// ---------- getVacuna ----------

	@Test
	void testGetVacuna() throws EntityNotFoundException {
		VacunaEntity esperada = vacunaList.get(0);
		VacunaEntity obtenida = vacunaService.getVacuna(esperada.getId());
		assertEquals(esperada.getId(), obtenida.getId());
		assertEquals(esperada.getNombre(), obtenida.getNombre());
	}

	@Test
	void testGetVacunaNoExistente() {
		assertThrows(EntityNotFoundException.class, () -> vacunaService.getVacuna(0L));
	}

	// ---------- updateVacuna ----------

	@Test
	void testUpdateVacuna() throws EntityNotFoundException, IllegalOperationException {
		VacunaEntity vacuna = vacunaList.get(0);
		VacunaEntity nuevosDatos = factory.manufacturePojo(VacunaEntity.class);
		nuevosDatos.setNombre("Moquillo " + System.nanoTime());

		VacunaEntity actualizada = vacunaService.updateVacuna(vacuna.getId(), nuevosDatos);

		assertEquals(vacuna.getId(), actualizada.getId());
		assertEquals(nuevosDatos.getNombre(), actualizada.getNombre());
	}

	@Test
	void testUpdateVacunaNoExistente() {
		VacunaEntity nuevosDatos = factory.manufacturePojo(VacunaEntity.class);
		assertThrows(EntityNotFoundException.class, () -> vacunaService.updateVacuna(0L, nuevosDatos));
	}

	@Test
	void testUpdateVacunaConNombreVacio() {
		VacunaEntity vacuna = vacunaList.get(0);
		VacunaEntity nuevosDatos = factory.manufacturePojo(VacunaEntity.class);
		nuevosDatos.setNombre("");

		assertThrows(IllegalOperationException.class,
				() -> vacunaService.updateVacuna(vacuna.getId(), nuevosDatos));
	}

	@Test
	void testUpdateVacunaConNombreDuplicado() {
		VacunaEntity vacuna = vacunaList.get(0);
		VacunaEntity nuevosDatos = factory.manufacturePojo(VacunaEntity.class);
		nuevosDatos.setNombre(vacunaList.get(1).getNombre());

		assertThrows(IllegalOperationException.class,
				() -> vacunaService.updateVacuna(vacuna.getId(), nuevosDatos));
	}

	// ---------- deleteVacuna ----------

	@Test
	void testDeleteVacuna() throws EntityNotFoundException, IllegalOperationException {
		VacunaEntity vacuna = vacunaList.get(0);
		vacunaService.deleteVacuna(vacuna.getId());
		assertNull(entityManager.find(VacunaEntity.class, vacuna.getId()));
	}

	@Test
	void testDeleteVacunaNoExistente() {
		assertThrows(EntityNotFoundException.class, () -> vacunaService.deleteVacuna(0L));
	}

	@Test
	void testDeleteVacunaConRegistrosAsociados() {
    VacunaEntity vacuna = vacunaList.get(0);
    RegistroVacunacionEntity registro = factory.manufacturePojo(RegistroVacunacionEntity.class);
    
    registro.setVacuna(vacuna);
    
    if (vacuna.getRegistrosVacunacion() == null) {
        vacuna.setRegistrosVacunacion(new ArrayList<>());
    }
    vacuna.getRegistrosVacunacion().add(registro);


    entityManager.persist(registro);
    entityManager.flush();
    entityManager.clear();
    assertThrows(IllegalOperationException.class, () -> vacunaService.deleteVacuna(vacuna.getId()));
}
}