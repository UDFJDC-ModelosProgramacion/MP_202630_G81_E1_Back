package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.repositories.*;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@Transactional
public class ActualizacionServiceTest {

    @Autowired
    private ActualizacionService actualizacionService;

    @Autowired
    private ActualizacionRepository actualizacionRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<ActualizacionEntity> data = new ArrayList<>();
    private AdoptanteEntity adoptante;
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        actualizacionRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        for (int i = 0; i < 3; i++) {
            ActualizacionEntity entity = factory.manufacturePojo(ActualizacionEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            data.add(actualizacionRepository.save(entity));
        }
    }

    @Test
    void testCreateActualizacion() {
        ActualizacionEntity entity = factory.manufacturePojo(ActualizacionEntity.class);
        entity.setMascota(mascota);
        entity.setAdoptante(adoptante);
        ActualizacionEntity result = actualizacionService.createActualizacion(entity);
        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
    }

    @Test
    void testGetActualizaciones() {
        assertEquals(3, actualizacionService.getActualizaciones().size());
    }

    @Test
    void testGetActualizacion() {
        ActualizacionEntity entity = data.get(0);
        assertEquals(entity.getId(), actualizacionService.getActualizacion(entity.getId()).getId());
    }

    @Test
    void testUpdateActualizacion() {
        ActualizacionEntity entity = data.get(0);
        entity.setTipo("MEDICA");
        ActualizacionEntity result = actualizacionService.updateActualizacion(entity.getId(), entity);
        assertEquals("MEDICA", result.getTipo());
    }

    @Test
    void testDeleteActualizacion() {
        ActualizacionEntity entity = data.get(0);
        actualizacionService.deleteActualizacion(entity.getId());
        assertNull(actualizacionService.getActualizacion(entity.getId()));
    }
}