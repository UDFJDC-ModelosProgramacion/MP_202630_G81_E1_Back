package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.HistoriaExitoDTO;
import co.edu.udistrital.mdp.pets.dto.HistoriaExitoDTODetail;
import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.HistoriaExitoRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@RestController
@RequestMapping("/mascotas/{mascotaId}/historias-exito")
public class HistoriaExitoController {

    private final HistoriaExitoRepository historiaRepository;
    private final MascotaRepository mascotaRepository;
    private final ModelMapper modelMapper;

    public HistoriaExitoController(HistoriaExitoRepository historiaRepository,
            MascotaRepository mascotaRepository, ModelMapper modelMapper) {
        this.historiaRepository = historiaRepository;
        this.mascotaRepository = mascotaRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<HistoriaExitoDTODetail> findAll(@PathVariable Long mascotaId) throws EntityNotFoundException {
        requireMascota(mascotaId);
        return historiaRepository.findByMascotaId(mascotaId).stream()
                .map(entity -> modelMapper.map(entity, HistoriaExitoDTODetail.class)).collect(Collectors.toList());
    }

    @GetMapping("/{historiaExitoId}")
    public HistoriaExitoDTODetail findOne(@PathVariable Long mascotaId, @PathVariable Long historiaExitoId)
            throws EntityNotFoundException {
        return detail(requireHistoria(mascotaId, historiaExitoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HistoriaExitoDTODetail create(@PathVariable Long mascotaId, @RequestBody HistoriaExitoDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity mascota = requireMascota(mascotaId);
        requireActive(mascota);
        HistoriaExitoEntity entity = modelMapper.map(dto, HistoriaExitoEntity.class);
        entity.setMascota(mascota);
        return detail(historiaRepository.save(entity));
    }

    @PutMapping("/{historiaExitoId}")
    public HistoriaExitoDTODetail update(@PathVariable Long mascotaId, @PathVariable Long historiaExitoId,
            @RequestBody HistoriaExitoDTO dto) throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity mascota = requireMascota(mascotaId);
        requireActive(mascota);
        HistoriaExitoEntity entity = requireHistoria(mascotaId, historiaExitoId);
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());
        entity.setMascota(mascota);
        return detail(historiaRepository.save(entity));
    }

    @DeleteMapping("/{historiaExitoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long mascotaId, @PathVariable Long historiaExitoId)
            throws EntityNotFoundException, IllegalOperationException {
        requireActive(requireMascota(mascotaId));
        requireHistoria(mascotaId, historiaExitoId);
        historiaRepository.deleteById(historiaExitoId);
    }

    private MascotaEntity requireMascota(Long id) throws EntityNotFoundException {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La mascota con el id dado no existe"));
    }

    private void requireActive(MascotaEntity mascota) throws IllegalOperationException {
        if ("Inactivo".equals(mascota.getEstado())) {
            throw new IllegalOperationException("La mascota se encuentra inactiva");
        }
    }

    private HistoriaExitoEntity requireHistoria(Long mascotaId, Long historiaId) throws EntityNotFoundException {
        HistoriaExitoEntity entity = historiaRepository.findByMascotaIdAndId(mascotaId, historiaId);
        if (entity == null) {
            throw new EntityNotFoundException("La historia de éxito con el id dado no existe");
        }
        return entity;
    }

    private HistoriaExitoDTODetail detail(HistoriaExitoEntity entity) {
        return modelMapper.map(entity, HistoriaExitoDTODetail.class);
    }
}
