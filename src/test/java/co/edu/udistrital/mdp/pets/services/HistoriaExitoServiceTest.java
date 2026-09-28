package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.repositories.HistoriaExitoRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class HistoriaExitoServiceTest {

    @Mock
    private HistoriaExitoRepository historiaExitoRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    private HistoriaExitoService service;

    @BeforeEach
    void setUp() {
        service = new HistoriaExitoService(
                historiaExitoRepository,
                mascotaRepository
        );
    }

    @Test
    void crearHistoriaExitoSavesHistoryForActiveMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        service.crearHistoriaExito(
                1L,
                "Adopción feliz",
                "Una nueva familia"
        );

        verify(historiaExitoRepository)
                .save(any(HistoriaExitoEntity.class));
    }

    @Test
    void crearHistoriaExitoRejectsInactiveMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Inactivo");

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.crearHistoriaExito(
                        1L,
                        "Título",
                        "Descripción"
                )
        );

        verifyNoInteractions(historiaExitoRepository);
    }

    @Test
    void crearHistoriaExitoRejectsMissingMascota() {
        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.crearHistoriaExito(
                        1L,
                        "Título",
                        "Descripción"
                )
        );

        verifyNoInteractions(historiaExitoRepository);
    }

    @Test
    void eliminarHistoriaExitoDeletesAssociatedHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        HistoriaExitoEntity history = new HistoriaExitoEntity();

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L))
                .thenReturn(history);

        service.eliminarHistoriaExito(2L, 1L);

        verify(historiaExitoRepository)
                .deleteById(2L);
    }

    @Test
    void eliminarHistoriaExitoRejectsMissingHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L))
                .thenReturn(null);

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.eliminarHistoriaExito(2L, 1L)
        );

        verify(historiaExitoRepository)
                .findByMascotaIdAndId(1L, 2L);

        verify(
                historiaExitoRepository,
                never()
        ).deleteById(2L);
    }

    @Test
    void eliminarHistoriaExitoRejectsInactiveMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Inactivo");

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.eliminarHistoriaExito(2L, 1L)
        );

        verifyNoInteractions(historiaExitoRepository);
    }

    @Test
    void actualizarHistoriaExitoSavesAssociatedHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        HistoriaExitoEntity submitted =
                new HistoriaExitoEntity();

        submitted.setId(2L);

        HistoriaExitoEntity stored =
                new HistoriaExitoEntity();

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L))
                .thenReturn(stored);

        service.actualizarHistoriaExito(
                submitted,
                1L
        );

        verify(historiaExitoRepository)
                .save(stored);
    }

    @Test
    void actualizarHistoriaExitoRejectsMissingHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        HistoriaExitoEntity submitted =
                new HistoriaExitoEntity();

        submitted.setId(2L);

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L))
                .thenReturn(null);

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.actualizarHistoriaExito(
                        submitted,
                        1L
                )
        );

        verify(
                historiaExitoRepository,
                never()
        ).save(any(HistoriaExitoEntity.class));
    }

    @Test
    void obtenerHistoriaExitoByIdAndIdMascotaReturnsHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        HistoriaExitoEntity expected =
                new HistoriaExitoEntity();

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L))
                .thenReturn(expected);

        assertEquals(
                expected,
                service.obtenerHistoriaExitoByIdAndIdMascota(
                        2L,
                        1L
                )
        );
    }

    @Test
    void obtenerHistoriaExitoByIdAndIdMascotaRejectsMissingHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L))
                .thenReturn(null);

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.obtenerHistoriaExitoByIdAndIdMascota(
                        2L,
                        1L
                )
        );
    }

    @Test
    void obtenerHistoriasExitoByIdMascotaReturnsHistories() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");

        List<HistoriaExitoEntity> expected =
                List.of(new HistoriaExitoEntity());

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        when(historiaExitoRepository.findByMascotaId(1L))
                .thenReturn(expected);

        assertEquals(
                expected,
                service.obtenerHistoriasExitoByIdMascota(1L)
        );
    }

    @Test
    void obtenerHistoriasExitoByIdMascotaRejectsInactiveMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Inactivo");

        when(mascotaRepository.findById(1L))
                .thenReturn(Optional.of(mascota));

        assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.obtenerHistoriasExitoByIdMascota(1L)
        );

        verifyNoInteractions(historiaExitoRepository);
    }
}
