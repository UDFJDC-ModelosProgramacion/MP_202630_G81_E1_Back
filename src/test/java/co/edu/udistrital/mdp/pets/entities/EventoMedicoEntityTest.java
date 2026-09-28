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
public class EventoMedicoEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<EventoMedicoEntity> data = new ArrayList<>();
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EventoMedicoEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
    }

    private void insertData() {
        mascota = factory.manufacturePojo(MascotaEntity.class);
        entityManager.persist(mascota);

        for (int i = 0; i < 3; i++) {
            EventoMedicoEntity entity = factory.manufacturePojo(EventoMedicoEntity.class);
            entity.setMascota(mascota);
            entityManager.persist(entity);
            data.add(entity);
        }
    }

    @Test
    void testCreateEventoMedico() {
        EventoMedicoEntity entity = factory.manufacturePojo(EventoMedicoEntity.class);
        entity.setMascota(mascota);
        EventoMedicoEntity result = entityManager.persistFlushFind(entity);
        assertNotNull(result);
        assertEquals(entity.getDescripcion(), result.getDescripcion());
        assertEquals(entity.getDiagnostico(), result.getDiagnostico());
        assertEquals(entity.getTratamiento(), result.getTratamiento());
    }

    @Test
    void testGetEventoMedico() {
        EventoMedicoEntity entity = data.get(0);
        EventoMedicoEntity result = entityManager.find(EventoMedicoEntity.class, entity.getId());
        assertNotNull(result);
        assertEquals(entity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void testUpdateEventoMedico() {
        EventoMedicoEntity entity = data.get(0);
        EventoMedicoEntity newData = factory.manufacturePojo(EventoMedicoEntity.class);
        entity.setDescripcion(newData.getDescripcion());
        entity.setDiagnostico(newData.getDiagnostico());
        entity.setTratamiento(newData.getTratamiento());
        entityManager.merge(entity);
        EventoMedicoEntity result = entityManager.find(EventoMedicoEntity.class, entity.getId());
        assertEquals(newData.getDescripcion(), result.getDescripcion());
        assertEquals(newData.getDiagnostico(), result.getDiagnostico());
        assertEquals(newData.getTratamiento(), result.getTratamiento());
    }

    @Test
    void testDeleteEventoMedico() {
        EventoMedicoEntity entity = data.get(0);
        entityManager.remove(entity);
        EventoMedicoEntity deleted = entityManager.find(EventoMedicoEntity.class, entity.getId());
        assertNull(deleted);
    }
}