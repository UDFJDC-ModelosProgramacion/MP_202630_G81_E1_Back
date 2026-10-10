package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
    void getEventosVidaByMascotaIdPropagatesRepositoryFailure() {
        Long id = 1L;
        RuntimeException failure = new IllegalStateException("database failure");
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaId(id)).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> service.getEventosVidaByMascotaId(id)));
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

    @ParameterizedTest
    @NullAndEmptySource
    void getEventosVidaByTipoRejectsNullOrEmptyType(String tipoEvento) {
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEvento(1L, tipoEvento));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaByTipoThrowsWhenAdoptanteDoesNotExist() {
        when(adoptanteRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEvento(1L, "Nacimiento"));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaByTipoPropagatesRepositoryFailure() {
        Long id = 1L;
        RuntimeException failure = new IllegalStateException("database failure");
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaIdAndTipo(id, "Nacimiento")).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEvento(id, "Nacimiento")));
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

    @ParameterizedTest
    @NullAndEmptySource
    void getEventosVidaBetweenRejectsNullOrEmptyType(String tipoEvento) {
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoBetween(
                        1L, tipoEvento, Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31")));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaBetweenRejectsInvalidType() {
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoBetween(
                        1L, "Cumpleaños", Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31")));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaBetweenThrowsWhenAdoptanteDoesNotExist() {
        when(adoptanteRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoBetween(
                        1L, "Vacunación", Date.valueOf("2024-01-01"), Date.valueOf("2024-12-31")));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaBetweenPropagatesRepositoryFailure() {
        Long id = 1L;
        Date start = Date.valueOf("2024-01-01");
        Date end = Date.valueOf("2024-12-31");
        RuntimeException failure = new IllegalStateException("database failure");
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaIdAndTipoAndFechaBetween(id, "Vacunación", start, end))
                .thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoBetween(
                        id, "Vacunación", start, end)));
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

    @ParameterizedTest
    @NullAndEmptySource
    void getEventosVidaAfterRejectsNullOrEmptyType(String tipoEvento) {
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoAfter(
                        1L, tipoEvento, Date.valueOf("2024-01-01")));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaAfterRejectsInvalidType() {
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoAfter(
                        1L, "Cumpleaños", Date.valueOf("2024-01-01")));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaAfterThrowsWhenAdoptanteDoesNotExist() {
        when(adoptanteRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoAfter(
                        1L, "Enfermedad", Date.valueOf("2024-01-01")));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void getEventosVidaAfterPropagatesRepositoryFailure() {
        Long id = 1L;
        Date start = Date.valueOf("2024-01-01");
        RuntimeException failure = new IllegalStateException("database failure");
        when(adoptanteRepository.existsById(id)).thenReturn(true);
        when(eventoVidaRepository.findByMascotaIdAndTipoAndFechaAfter(id, "Enfermedad", start))
                .thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> service.getEventosVidaByMascotaIdAndTipoEventoAndFechaEventoAfter(
                        id, "Enfermedad", start)));
    }

    @Test
    void createEventoVidaSavesValidEvent() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Adopción");
        event.setFecha(Date.valueOf("2024-01-01"));

        service.createEventoVida(event);

        verify(eventoVidaRepository).save(event);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void createEventoVidaRejectsNullOrEmptyType(String tipoEvento) {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo(tipoEvento);

        assertThrows(IllegalArgumentException.class, () -> service.createEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void createEventoVidaRejectsInvalidType() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Cumpleaños");

        assertThrows(IllegalArgumentException.class, () -> service.createEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void createEventoVidaRejectsMissingDate() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Nacimiento");

        assertThrows(IllegalArgumentException.class, () -> service.createEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
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
    void createEventoVidaPropagatesRepositoryFailure() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Muerte");
        event.setFecha(Date.valueOf("2024-01-01"));
        RuntimeException failure = new IllegalStateException("database failure");
        when(eventoVidaRepository.save(event)).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> service.createEventoVida(event)));
    }

    @Test
    void updateEventoVidaSavesValidEvent() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Nacimiento");
        event.setFecha(Date.valueOf("2024-01-01"));

        service.updateEventoVida(event);

        verify(eventoVidaRepository).save(event);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void updateEventoVidaRejectsNullOrEmptyType(String tipoEvento) {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo(tipoEvento);

        assertThrows(IllegalArgumentException.class, () -> service.updateEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void updateEventoVidaRejectsInvalidType() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Cumpleaños");

        assertThrows(IllegalArgumentException.class, () -> service.updateEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void updateEventoVidaRejectsMissingDate() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Nacimiento");

        assertThrows(IllegalArgumentException.class, () -> service.updateEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void updateEventoVidaRejectsFutureDate() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Muerte");
        event.setFecha(Date.valueOf("2999-01-01"));

        assertThrows(IllegalArgumentException.class, () -> service.updateEventoVida(event));
        verifyNoInteractions(eventoVidaRepository);
    }

    @Test
    void updateEventoVidaPropagatesRepositoryFailure() {
        EventoVidaEntity event = new EventoVidaEntity() {};
        event.setTipo("Nacimiento");
        event.setFecha(Date.valueOf("2024-01-01"));
        RuntimeException failure = new IllegalStateException("database failure");
        when(eventoVidaRepository.save(event)).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> service.updateEventoVida(event)));
    }

    @Test
    void deleteEventoVidaDeletesById() {
        service.deleteEventoVida(4L);

        verify(eventoVidaRepository).deleteById(4L);
    }

    @Test
    void deleteEventoVidaPropagatesRepositoryFailure() {
        Long id = 4L;
        RuntimeException failure = new IllegalStateException("database failure");
        org.mockito.Mockito.doThrow(failure).when(eventoVidaRepository).deleteById(id);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> service.deleteEventoVida(id)));
    }
}