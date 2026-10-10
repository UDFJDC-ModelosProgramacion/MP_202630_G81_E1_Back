package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MensajeEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.MensajeRepository;

@ExtendWith(MockitoExtension.class)
class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private AdoptanteRepository adoptanteRepository;

    private MensajeService service;

    @BeforeEach
    void setUp() {
        service = new MensajeService(mensajeRepository, mascotaRepository, adoptanteRepository);
    }

    @Test
    void getAllMensajesReturnsMessagesForExistingAdoptante() {
        List<MensajeEntity> expected = List.of(new MensajeEntity());
        when(adoptanteRepository.existsById(1L)).thenReturn(true);
        when(mensajeRepository.findByAdoptanteId(1L)).thenReturn(expected);

        assertEquals(expected, service.getAllMensajes(1L));
    }

    @Test
    void getAllMensajesRejectsMissingAdoptante() {
        when(adoptanteRepository.existsById(1L)).thenReturn(false);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.getAllMensajes(1L));

        assertEquals("El adoptante con ID: 1 no existe", exception.getMessage());
        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void getAllMensajesWrapsRepositoryFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(adoptanteRepository.existsById(1L)).thenReturn(true);
        when(mensajeRepository.findByAdoptanteId(1L)).thenThrow(failure);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.getAllMensajes(1L));

        assertEquals("Error al listar los mensajes del adoptante con ID: 1",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void getMensajesByMascotaIdReturnsMessagesForExistingMascota() {
        List<MensajeEntity> expected = List.of(new MensajeEntity());
        when(mascotaRepository.existsById(1L)).thenReturn(true);
        when(mensajeRepository.findByMascotaId(1L)).thenReturn(expected);

        assertEquals(expected, service.getMensajesByMascotaId(1L));
    }

    @Test
    void getMensajesByMascotaIdRejectsMissingMascota() {
        when(mascotaRepository.existsById(1L)).thenReturn(false);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.getMensajesByMascotaId(1L));

        assertEquals("La mascota con ID: 1 no existe", exception.getMessage());
        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void getMensajesByMascotaIdWrapsRepositoryFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.existsById(1L)).thenReturn(true);
        when(mensajeRepository.findByMascotaId(1L)).thenThrow(failure);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.getMensajesByMascotaId(1L));

        assertEquals("Error al listar los mensajes de la mascota con ID: 1",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void eliminarMensajeDeletesUnreadMessageOfAdoptante() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        message.setLeido(false);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.eliminarMensaje(2L, 1L);

        verify(mensajeRepository).delete(message);
    }

    @Test
    void eliminarMensajeRejectsReadMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        message.setLeido(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        assertThrows(RuntimeException.class, () -> service.eliminarMensaje(2L, 1L));
    }

    @Test
    void eliminarMensajeRejectsMissingMessage() {
        when(mensajeRepository.findById(2L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.eliminarMensaje(2L, 1L));

        assertEquals("El mensaje con ID: 2 no existe", exception.getMessage());
        verifyNoInteractions(adoptanteRepository);
        verify(mensajeRepository, never()).delete(any());
    }

    @Test
    void eliminarMensajeRejectsMissingAdoptante() {
        MensajeEntity message = new MensajeEntity();
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.eliminarMensaje(2L, 1L));

        assertEquals("El adoptante con ID: 1 no existe", exception.getMessage());
        verify(mensajeRepository, never()).delete(any());
    }

    @Test
    void eliminarMensajeRejectsMessageWithoutOwner() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.eliminarMensaje(2L, 1L));

        assertEquals(
                "El mensaje con ID: 2 no pertenece al adoptante con ID: 1",
                exception.getMessage());
        verify(mensajeRepository, never()).delete(any());
    }

    @Test
    void eliminarMensajeRejectsDifferentOwner() {
        AdoptanteEntity owner = new AdoptanteEntity();
        owner.setId(3L);
        AdoptanteEntity requestedAdoptante = new AdoptanteEntity();
        requestedAdoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(owner);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(requestedAdoptante));

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.eliminarMensaje(2L, 1L));

        assertEquals(
                "El mensaje con ID: 2 no pertenece al adoptante con ID: 1",
                exception.getMessage());
        verify(mensajeRepository, never()).delete(any());
    }

    @Test
    void eliminarMensajeWrapsRepositoryFailure() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        RuntimeException failure = new IllegalStateException("database failure");
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));
        doThrow(failure).when(mensajeRepository).delete(message);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.eliminarMensaje(2L, 1L));

        assertEquals("Error al eliminar el mensaje con ID: 2", exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void marcarMensajeComoLeidoUpdatesMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.marcarMensajeComoLeido(2L, 1L);

        assertEquals(true, message.isLeido());
        verify(mensajeRepository).save(message);
    }

    @Test
    void marcarMensajeComoLeidoRejectsMissingMessage() {
        when(mensajeRepository.findById(2L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.marcarMensajeComoLeido(2L, 1L));

        assertEquals("El mensaje con ID: 2 no existe", exception.getMessage());
        verifyNoInteractions(adoptanteRepository);
    }

    @Test
    void marcarMensajeComoLeidoRejectsMissingAdoptante() {
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(new MensajeEntity()));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.marcarMensajeComoLeido(2L, 1L));

        assertEquals("El adoptante con ID: 1 no existe", exception.getMessage());
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    void marcarMensajeComoLeidoWrapsRepositoryFailure() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        RuntimeException failure = new IllegalStateException("database failure");
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));
        when(mensajeRepository.save(message)).thenThrow(failure);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.marcarMensajeComoLeido(2L, 1L));

        assertEquals("Error al marcar el mensaje con ID: 2 como leído",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void marcarMensajeComoNoLeidoUpdatesMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        message.setLeido(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.marcarMensajeComoNoLeido(2L, 1L);

        assertEquals(false, message.isLeido());
        verify(mensajeRepository).save(message);
    }

    @Test
    void marcarMensajeComoNoLeidoRejectsMissingMessage() {
        when(mensajeRepository.findById(2L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.marcarMensajeComoNoLeido(2L, 1L));

        assertEquals("El mensaje con ID: 2 no existe", exception.getMessage());
        verifyNoInteractions(adoptanteRepository);
    }

    @Test
    void marcarMensajeComoNoLeidoWrapsRepositoryFailure() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        RuntimeException failure = new IllegalStateException("database failure");
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));
        when(mensajeRepository.save(message)).thenThrow(failure);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.marcarMensajeComoNoLeido(2L, 1L));

        assertEquals("Error al marcar el mensaje con ID: 2 como no leído",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void editarMensajeSavesUnreadMessageOfAdoptante() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity storedMessage = new MensajeEntity();
        storedMessage.setId(2L);
        storedMessage.setAdoptante(adoptante);
        storedMessage.setLeido(false);

        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        message.setLeido(false);

        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(storedMessage));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.editarMensaje(message, 1L);

        verify(mensajeRepository).save(message);
    }

    @Test
    void editarMensajeRejectsReadMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity storedMessage = new MensajeEntity();
        storedMessage.setId(2L);
        storedMessage.setAdoptante(adoptante);
        storedMessage.setLeido(true);

        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        message.setLeido(true);

        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(storedMessage));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        assertThrows(RuntimeException.class, () -> service.editarMensaje(message, 1L));
    }

    @Test
    void editarMensajeRejectsMissingMessage() {
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.editarMensaje(message, 1L));

        assertEquals("El mensaje con ID: 2 no existe", exception.getMessage());
        verifyNoInteractions(adoptanteRepository);
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    void editarMensajeRejectsMissingAdoptante() {
        MensajeEntity storedMessage = new MensajeEntity();
        storedMessage.setId(2L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(storedMessage));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.empty());

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.editarMensaje(message, 1L));

        assertEquals("El adoptante con ID: 1 no existe", exception.getMessage());
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    void editarMensajeRejectsMessageWithoutOwner() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity storedMessage = new MensajeEntity();
        storedMessage.setId(2L);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(storedMessage));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.editarMensaje(message, 1L));

        assertEquals(
                "El mensaje con ID: 2 no pertenece al adoptante con ID: 1",
                exception.getMessage());
        verify(mensajeRepository, never()).save(any());
    }

    @Test
    void editarMensajeWrapsRepositoryFailure() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        adoptante.setId(1L);
        MensajeEntity storedMessage = new MensajeEntity();
        storedMessage.setId(2L);
        storedMessage.setAdoptante(adoptante);
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        RuntimeException failure = new IllegalStateException("database failure");
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(storedMessage));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));
        when(mensajeRepository.save(message)).thenThrow(failure);

        MensajeService.MensajeServiceException exception = assertThrows(
                MensajeService.MensajeServiceException.class,
                () -> service.editarMensaje(message, 1L));

        assertEquals("Error al editar el mensaje con ID: 2", exception.getMessage());
        assertSame(failure, exception.getCause());
    }
}