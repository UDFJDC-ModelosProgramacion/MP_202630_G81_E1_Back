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
public class PruebaConvivenciaServiceTest {

    @Autowired
    private PruebaConvivenciaService pruebaService;

    @Autowired
    private PruebaConvivenciaRepository pruebaRepository;

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<PruebaConvivenciaEntity> data = new ArrayList<>();

    private AdopcionEntity adopcion;

    @BeforeEach
    void setUp() {

        pruebaRepository.deleteAll();
        adopcionRepository.deleteAll();
        solicitudRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        AdoptanteEntity adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        MascotaEntity mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        SolicitudAdopcionEntity solicitud = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);
        solicitud = solicitudRepository.save(solicitud);

        adopcion = factory.manufacturePojo(AdopcionEntity.class);
        adopcion.setSolicitud(solicitud);
        adopcion = adopcionRepository.save(adopcion);

        PruebaConvivenciaEntity entity = factory.manufacturePojo(PruebaConvivenciaEntity.class);
        entity.setAdopcion(adopcion);
        data.add(pruebaRepository.save(entity));
    }

    @Test
    void testCreatePrueba() {
        PruebaConvivenciaEntity entity = factory.manufacturePojo(PruebaConvivenciaEntity.class);
        PruebaConvivenciaEntity result = pruebaService.createPrueba(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testGetPruebas() {
        assertEquals(1, pruebaService.getPruebas().size());
    }

    @Test
    void testGetPrueba() {
        PruebaConvivenciaEntity entity = data.get(0);
        assertEquals(entity.getId(), pruebaService.getPrueba(entity.getId()).getId());
    }

    @Test
    void testUpdatePrueba() {
        PruebaConvivenciaEntity entity = data.get(0);
        entity.setEstado("FINALIZADA");

        PruebaConvivenciaEntity result = pruebaService.updatePrueba(entity.getId(), entity);

        assertEquals("FINALIZADA", result.getEstado());
    }

    @Test
    void testDeletePrueba() {
        PruebaConvivenciaEntity entity = data.get(0);
        pruebaService.deletePrueba(entity.getId());

        assertNull(pruebaService.getPrueba(entity.getId()));
    }
}