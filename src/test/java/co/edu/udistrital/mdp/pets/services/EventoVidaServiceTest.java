package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.EventoVidaEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.EventoVidaRepository;

@ExtendWith(MockitoExtension.class)
class EventoVidaServiceTest {

    @Mock
    private EventoVidaRepository eventoVidaRepository;

    @Mock
    private AdoptanteRepository adoptanteRepository;

    private EventoVidaService service;

    @BeforeEach
    void setUp() {
        service = new EventoVidaService(eventoVidaRepository, adoptanteRepository);
    }

    @Test
    void getEventosVidaByMascotaIdReturnsEventsWhenAdoptanteExists() {
        Long id = 1L;
        List<EventoVidaEntity> expected = List.of();
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaId(id)).thenReturn(expected);

        assertEquals(expected, service.getEventosVidaByMascotaId(id));
        verify(eventoVidaRepository).findByMascotaId(id);
    }

    @Test
    void getEventosVidaByMascotaIdThrowsWhenAdoptanteDoesNotExist() {
        Long id = 1L;
        when(adoptanteRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.getEventosVidaByMascotaId(id));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaByTipoReturnsEventsForValidType() {
        Long id = 1L;
        List<EventoVidaEntity> expected = List.of();
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaIdAndTipo(id, "Nacimiento")).thenReturn(expected);

        assertEquals(expected, service.getEventosVidaByMascotaIdAndTipoEvento(id, "Nacimiento"));
        verify(eventoVidaRepository).findByMascotaIdAndTipo(id, "Nacimiento");
    }

    @Test
    void getEventosVidaByTipoThrowsForInvalidType() {
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEvento(1L, "Cumpleaños"));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaBetweenReturnsEvents() {
        Long id = 1L;
        Date start = Date.valueOf("2024-01-01");
        Date end = Date.valueOf("2024-12-31");
        List<EventoVidaEntity> expected = List.of();
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaIdAndTipoAndFechaBetween(id, "Vacunación", start, end))
                .thenReturn(expected);

        assertEquals(expected, service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoBetween(
                id, "Vacunación", start, end));
        verify(eventoVidaRepository).findByMascotaIdAndTipoAndFechaBetween(id, "Vacunación", start, end);
    }

    @Test
    void getEventosVidaAfterReturnsEvents() {
        Long id = 1L;
        Date start = Date.valueOf("2024-01-01");
        List<EventoVidaEntity> expected = List.of();
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaIdAndTipoAndFechaAfter(id, "Enfermedad", start))
                .thenReturn(expected);

        assertEquals(expected, service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoAfter(
                id, "Enfermedad", start));
        verify(eventoVidaRepository).findByMascotaIdAndTipoAndFechaAfter(id, "Enfermedad", start);
    }

    @Test
    void createEventoVidaSavesValidEvent() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Adopción");
        event.setFecha(Date.valueOf("2024-01-01"));

        service.createEventoVida(event);

        verify(eventoVidaRepository).save(event);
    }

    @Test
    void createEventoVidaRejectsFutureDate() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Muerte");
        event.setFecha(Date.valueOf("2999-01-01"));

        assertThrows(IllegalArgumentException.class, () -> service.createEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void updateEventoVidaSavesValidEvent() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Nacimiento");
        event.setFecha(Date.valueOf("2024-01-01"));

        service.updateEventoVida(event);

        verify(eventoVidaRepository).save(event);
    }

    @Test
    void deleteEventoVidaDeletesById() {
        service.deleteEventoVida(4L);

        verify(eventoVidaRepository).deleteById(4L);
    }
}