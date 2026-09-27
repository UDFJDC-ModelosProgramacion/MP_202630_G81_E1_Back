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
import co.edu.udistrital.mdp.pets.entities.RetornoEntity;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopcionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.RetornoRepository;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@Transactional
public class RetornoServiceTest {

    @Autowired
    private RetornoService retornoService;

    @Autowired
    private RetornoRepository retornoRepository;

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<RetornoEntity> data = new ArrayList<>();

    private AdopcionEntity adopcion;

    @BeforeEach
    void setUp() {

        retornoRepository.deleteAll();
        adopcionRepository.deleteAll();
        solicitudRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        // Adopción independiente para testCreateRetorno
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

        // Cada retorno tendrá una adopción diferente
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

            RetornoEntity entity =
                    factory.manufacturePojo(RetornoEntity.class);

            entity.setAdopcion(nuevaAdopcion);

            data.add(retornoRepository.save(entity));
        }
    }

    @Test
    void testCreateRetorno() {

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

        RetornoEntity entity =
                factory.manufacturePojo(RetornoEntity.class);

        entity.setAdopcion(nuevaAdopcion);

        RetornoEntity result = retornoService.createRetorno(entity);

        assertNotNull(result);
        assertEquals(entity.getMotivo(), result.getMotivo());
    }

    @Test
    void testGetRetornos() {
        List<RetornoEntity> list = retornoService.getRetornos();
        assertEquals(3, list.size());
    }

    @Test
    void testGetRetorno() {

        RetornoEntity entity = data.get(0);

        RetornoEntity result =
                retornoService.getRetorno(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testUpdateRetorno() {

        RetornoEntity entity = data.get(0);
        entity.setMotivo("Cambio de domicilio");

        RetornoEntity result =
                retornoService.updateRetorno(entity.getId(), entity);

        assertNotNull(result);
        assertEquals("Cambio de domicilio", result.getMotivo());
    }

    @Test
    void testDeleteRetorno() {

        RetornoEntity entity = data.get(0);

        retornoService.deleteRetorno(entity.getId());

        RetornoEntity deleted =
                retornoService.getRetorno(entity.getId());

        assertNull(deleted);
    }
}