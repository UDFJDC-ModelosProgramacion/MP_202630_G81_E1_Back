package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.PruebaConvivenciaEntity;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopcionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.PruebaConvivenciaRepository;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;
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

        // Adopción independiente para testCreatePrueba
        AdoptanteEntity adoptante =
                adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));

        MascotaEntity mascota =
                mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        SolicitudAdopcionEntity solicitud =
                factory.manufacturePojo(SolicitudAdopcionEntity.class);

        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);

        solicitud = solicitudRepository.save(solicitud);

        adopcion = factory.manufacturePojo(AdopcionEntity.class);
        adopcion.setSolicitud(solicitud);

        adopcion = adopcionRepository.save(adopcion);

        // Cada prueba tendrá una adopción diferente
        for (int i = 0; i < 3; i++) {

            AdoptanteEntity nuevoAdoptante =
                    adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));

            MascotaEntity nuevaMascota =
                    mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

            SolicitudAdopcionEntity nuevaSolicitud =
                    factory.manufacturePojo(SolicitudAdopcionEntity.class);

            nuevaSolicitud.setAdoptante(nuevoAdoptante);
            nuevaSolicitud.setMascota(nuevaMascota);

            nuevaSolicitud = solicitudRepository.save(nuevaSolicitud);

            AdopcionEntity nuevaAdopcion =
                    factory.manufacturePojo(AdopcionEntity.class);

            nuevaAdopcion.setSolicitud(nuevaSolicitud);

            nuevaAdopcion = adopcionRepository.save(nuevaAdopcion);

            PruebaConvivenciaEntity entity =
                    factory.manufacturePojo(PruebaConvivenciaEntity.class);

            entity.setAdopcion(nuevaAdopcion);

            data.add(pruebaRepository.save(entity));
        }
    }

    @Test
    void testCreatePrueba() {

        AdoptanteEntity adoptante =
                adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));

        MascotaEntity mascota =
                mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        SolicitudAdopcionEntity solicitud =
                factory.manufacturePojo(SolicitudAdopcionEntity.class);

        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);

        solicitud = solicitudRepository.save(solicitud);

        AdopcionEntity nuevaAdopcion =
                factory.manufacturePojo(AdopcionEntity.class);

        nuevaAdopcion.setSolicitud(solicitud);

        nuevaAdopcion = adopcionRepository.save(nuevaAdopcion);

        PruebaConvivenciaEntity entity =
                factory.manufacturePojo(PruebaConvivenciaEntity.class);

        entity.setAdopcion(nuevaAdopcion);

        PruebaConvivenciaEntity result = pruebaService.createPrueba(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testGetPruebas() {
        List<PruebaConvivenciaEntity> list = pruebaService.getPruebas();
        assertEquals(3, list.size());
    }

    @Test
    void testGetPrueba() {

        PruebaConvivenciaEntity entity = data.get(0);

        PruebaConvivenciaEntity result =
                pruebaService.getPrueba(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testUpdatePrueba() {

        PruebaConvivenciaEntity entity = data.get(0);
        entity.setEstado("FINALIZADA");

        PruebaConvivenciaEntity result =
                pruebaService.updatePrueba(entity.getId(), entity);

        assertNotNull(result);
        assertEquals("FINALIZADA", result.getEstado());
    }

    @Test
    void testDeletePrueba() {

        PruebaConvivenciaEntity entity = data.get(0);

        pruebaService.deletePrueba(entity.getId());

        PruebaConvivenciaEntity deleted =
                pruebaService.getPrueba(entity.getId());

        assertNull(deleted);
    }
}