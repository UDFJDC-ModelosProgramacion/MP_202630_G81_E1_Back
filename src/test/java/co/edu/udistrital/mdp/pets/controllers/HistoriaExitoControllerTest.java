package co.edu.udistrital.mdp.pets.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.edu.udistrital.mdp.pets.dto.HistoriaExitoDTO;
import co.edu.udistrital.mdp.pets.dto.HistoriaExitoDTODetail;
import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.repositories.HistoriaExitoRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class HistoriaExitoControllerTest {

    @Mock
    private HistoriaExitoRepository historiaRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    private HistoriaExitoController controller;

    @BeforeEach
    void setUp() {
        controller = new HistoriaExitoController(historiaRepository, mascotaRepository, new ModelMapper());
    }

    @Test
    void updateReturnsUpdatedHistory() throws Exception {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setId(1L);
        mascota.setEstado("Disponible");

        HistoriaExitoEntity historia = new HistoriaExitoEntity();
        historia.setId(2L);
        historia.setMascota(mascota);

        HistoriaExitoDTO dto = new HistoriaExitoDTO();
        dto.setTitulo("Historia actualizada");
        dto.setDescripcion("Descripción actualizada");
        dto.setFecha(Date.valueOf("2024-03-20"));

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(historia);
        when(historiaRepository.save(historia)).thenReturn(historia);

        HistoriaExitoDTODetail response = controller.update(1L, 2L, dto);

        assertEquals(2L, response.getId());
        assertEquals("Historia actualizada", response.getTitulo());
        assertEquals("Descripción actualizada", response.getDescripcion());
        assertEquals(Date.valueOf("2024-03-20"), response.getFecha());
    }
}