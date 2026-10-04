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

import co.edu.udistrital.mdp.pets.dto.FotografiaDTO;
import co.edu.udistrital.mdp.pets.dto.FotografiaDTODetail;
import co.edu.udistrital.mdp.pets.entities.FotografiaEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.FotografiaRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@RestController
@RequestMapping("/mascotas/{mascotaId}/fotografias")
public class FotografiaController {

    private final FotografiaRepository fotografiaRepository;
    private final MascotaRepository mascotaRepository;
    private final ModelMapper modelMapper;

    public FotografiaController(FotografiaRepository fotografiaRepository,
            MascotaRepository mascotaRepository, ModelMapper modelMapper) {
        this.fotografiaRepository = fotografiaRepository;
        this.mascotaRepository = mascotaRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<FotografiaDTODetail> findAll(@PathVariable Long mascotaId) throws EntityNotFoundException {
        requireMascota(mascotaId);
        return fotografiaRepository.findAllByMascotaId(mascotaId).stream()
                .map(entity -> modelMapper.map(entity, FotografiaDTODetail.class)).collect(Collectors.toList());
    }

    @GetMapping("/{fotografiaId}")
    public FotografiaDTODetail findOne(@PathVariable Long mascotaId, @PathVariable Long fotografiaId)
            throws EntityNotFoundException {
        return detail(requireFotografia(mascotaId, fotografiaId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FotografiaDTODetail create(@PathVariable Long mascotaId, @RequestBody FotografiaDTO dto)
            throws EntityNotFoundException {
        MascotaEntity mascota = requireMascota(mascotaId);
        FotografiaEntity entity = modelMapper.map(dto, FotografiaEntity.class);
        entity.setMascota(mascota);
        return detail(fotografiaRepository.save(entity));
    }

    @PutMapping("/{fotografiaId}")
    public FotografiaDTODetail update(@PathVariable Long mascotaId, @PathVariable Long fotografiaId,
            @RequestBody FotografiaDTO dto) throws EntityNotFoundException {
        requireMascota(mascotaId);
        FotografiaEntity entity = requireFotografia(mascotaId, fotografiaId);
        entity.setUrl(dto.getUrl());
        entity.setPrincipal(dto.isPrincipal());
        entity.setDescripcion(dto.getDescripcion());
        return detail(fotografiaRepository.save(entity));
    }

    @PutMapping("/{fotografiaId}/activar")
    public FotografiaDTODetail activate(@PathVariable Long mascotaId, @PathVariable Long fotografiaId)
            throws EntityNotFoundException {
        FotografiaEntity entity = requireFotografia(mascotaId, fotografiaId);
        fotografiaRepository.findAllByMascotaId(mascotaId).forEach(fotografia -> fotografia.setPrincipal(false));
        entity.setPrincipal(true);
        return detail(fotografiaRepository.save(entity));
    }

    @DeleteMapping("/{fotografiaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long mascotaId, @PathVariable Long fotografiaId)
            throws EntityNotFoundException {
        requireFotografia(mascotaId, fotografiaId);
        fotografiaRepository.deleteById(fotografiaId);
    }

    private MascotaEntity requireMascota(Long id) throws EntityNotFoundException {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La mascota con el id dado no existe"));
    }

    private FotografiaEntity requireFotografia(Long mascotaId, Long fotografiaId) throws EntityNotFoundException {
        requireMascota(mascotaId);
        return fotografiaRepository.findById(fotografiaId)
                .filter(fotografia -> fotografia.getMascota() != null
                        && mascotaId.equals(fotografia.getMascota().getId()))
                .orElseThrow(() -> new EntityNotFoundException("La fotografía con el id dado no existe"));
    }

    private FotografiaDTODetail detail(FotografiaEntity entity) {
        return modelMapper.map(entity, FotografiaDTODetail.class);
    }
}
