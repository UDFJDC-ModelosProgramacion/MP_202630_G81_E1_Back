package co.edu.udistrital.mdp.pets.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.edu.udistrital.mdp.pets.dto.MensajeDTO;
import co.edu.udistrital.mdp.pets.dto.MensajeDTODetail;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.MensajeEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.MensajeRepository;

@ExtendWith(MockitoExtension.class)
class MensajeControllerTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private AdoptanteRepository adoptanteRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    private MensajeController controller;

    @BeforeEach
    void setUp() {
        controller = new MensajeController(mensajeRepository, adoptanteRepository, mascotaRepository,
                new ModelMapper());
    }

    @Test
    void updatePreservesRelationshipsWhenDtoOmitsTheirIds() throws Exception {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MascotaEntity mascota = new MascotaEntity();
        mascota.setId(2L);

        MensajeEntity mensaje = new MensajeEntity();
        mensaje.setId(3L);
        mensaje.setAdoptante(adoptante);
        mensaje.setMascota(mascota);
        mensaje.setLeido(false);

        MensajeDTO dto = new MensajeDTO();
        dto.setAsunto("Consulta actualizada");
        dto.setContenido("Contenido actualizado");
        dto.setFecha(Date.valueOf("2024-04-10"));
        dto.setLeido(false);

        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));
        when(mensajeRepository.findById(3L)).thenReturn(Optional.of(mensaje));
        when(mensajeRepository.save(mensaje)).thenReturn(mensaje);

        MensajeDTODetail response = controller.update(1L, 3L, dto);

        assertEquals(3L, response.getId());
        assertEquals("Consulta actualizada", response.getAsunto());
        assertEquals("Contenido actualizado", response.getContenido());
        assertEquals(Date.valueOf("2024-04-10"), response.getFecha());
        assertSame(adoptante, mensaje.getAdoptante());
        assertSame(mascota, mensaje.getMascota());
    }
}