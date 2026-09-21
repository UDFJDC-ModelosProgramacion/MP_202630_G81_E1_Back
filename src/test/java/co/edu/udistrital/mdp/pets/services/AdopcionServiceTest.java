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
public class AdopcionServiceTest {

    @Autowired
    private AdopcionService adopcionService;

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<AdopcionEntity> data = new ArrayList<>();

    private SolicitudAdopcionEntity solicitud;

    @BeforeEach
    void setUp() {

        adopcionRepository.deleteAll();
        solicitudRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        AdoptanteEntity adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        MascotaEntity mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        solicitud = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);
        solicitud = solicitudRepository.save(solicitud);

        for (int i = 0; i < 3; i++) {
            AdopcionEntity entity = factory.manufacturePojo(AdopcionEntity.class);
            entity.setSolicitud(solicitud);
            data.add(adopcionRepository.save(entity));
        }
    }

    @Test
    void testCreateAdopcion() {
        AdopcionEntity entity = factory.manufacturePojo(AdopcionEntity.class);
        entity.setSolicitud(solicitud);

        AdopcionEntity result = adopcionService.createAdopcion(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testGetAdopciones() {
        assertEquals(3, adopcionService.getAdopciones().size());
    }

    @Test
    void testGetAdopcion() {
        AdopcionEntity entity = data.get(0);
        assertEquals(entity.getId(), adopcionService.getAdopcion(entity.getId()).getId());
    }

    @Test
    void testUpdateAdopcion() {
        AdopcionEntity entity = data.get(0);
        entity.setEstado("FINALIZADA");

        AdopcionEntity result = adopcionService.updateAdopcion(entity.getId(), entity);

        assertEquals("FINALIZADA", result.getEstado());
    }

    @Test
    void testDeleteAdopcion() {
        AdopcionEntity entity = data.get(0);
        adopcionService.deleteAdopcion(entity.getId());

        assertNull(adopcionService.getAdopcion(entity.getId()));
    }
}