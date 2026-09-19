package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        assertThrows(RuntimeException.class, () -> service.getAllMensajes(1L));
        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void getMensajesByMascotaIdReturnsMessagesForExistingMascota() {
        List<MensajeEntity> expected = List.of(new MensajeEntity());
        when(mascotaRepository.existsById(1L)).thenReturn(true);
        when(mensajeRepository.findByMascotaId(1L)).thenReturn(expected);

        assertEquals(expected, service.getMensajesByMascotaId(1L));
    }

    @Test
    void eliminarMensajeDeletesUnreadMessageOfAdoptante() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        MensajeEntity message = new MensajeEntity();
        message.setAdoptante(adoptante);
        message.setLeido(false);
        when(mensajeRepository.existsById(2L)).thenReturn(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.eliminarMensaje(2L, 1L);

        verify(mensajeRepository).deleteById(2L);
    }

    @Test
    void eliminarMensajeRejectsReadMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        MensajeEntity message = new MensajeEntity();
        message.setAdoptante(adoptante);
        message.setLeido(true);
        when(mensajeRepository.existsById(2L)).thenReturn(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        assertThrows(RuntimeException.class, () -> service.eliminarMensaje(2L, 1L));
    }

    @Test
    void marcarMensajeComoLeidoUpdatesMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        MensajeEntity message = new MensajeEntity();
        message.setAdoptante(adoptante);
        when(mensajeRepository.existsById(2L)).thenReturn(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.marcarMensajeComoLeido(2L, 1L);

        assertEquals(true, message.isLeido());
        verify(mensajeRepository).save(message);
    }

    @Test
    void marcarMensajeComoNoLeidoUpdatesMessage() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        MensajeEntity message = new MensajeEntity();
        message.setAdoptante(adoptante);
        message.setLeido(true);
        when(mensajeRepository.existsById(2L)).thenReturn(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.marcarMensajeComoNoLeido(2L, 1L);

        assertEquals(false, message.isLeido());
        verify(mensajeRepository).save(message);
    }

    @Test
    void editarMensajeSavesUnreadMessageOfAdoptante() {
        AdoptanteEntity adoptante = new AdoptanteEntity();
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setAdoptante(adoptante);
        message.setLeido(false);
        when(mensajeRepository.existsById(2L)).thenReturn(true);
        when(mensajeRepository.findById(2L)).thenReturn(Optional.of(message));
        when(adoptanteRepository.existsById(1L)).thenReturn(true);
        when(adoptanteRepository.findById(1L)).thenReturn(Optional.of(adoptante));

        service.editarMensaje(message, 1L);

        verify(mensajeRepository).save(message);
    }

    @Test
    void editarMensajeRejectsReadMessage() {
        MensajeEntity message = new MensajeEntity();
        message.setId(2L);
        message.setLeido(true);
        when(mensajeRepository.existsById(2L)).thenReturn(true);
        when(adoptanteRepository.existsById(1L)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.editarMensaje(message, 1L));
    }
}