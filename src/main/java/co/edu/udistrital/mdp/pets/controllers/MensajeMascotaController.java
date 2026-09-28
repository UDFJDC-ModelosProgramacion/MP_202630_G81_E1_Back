package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.MensajeDTODetail;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.MensajeRepository;

@RestController
@RequestMapping("/mascotas/{mascotaId}/mensajes")
public class MensajeMascotaController {

    private final MensajeRepository mensajeRepository;
    private final MascotaRepository mascotaRepository;
    private final ModelMapper modelMapper;

    public MensajeMascotaController(MensajeRepository mensajeRepository,
            MascotaRepository mascotaRepository, ModelMapper modelMapper) {
        this.mensajeRepository = mensajeRepository;
        this.mascotaRepository = mascotaRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MensajeDTODetail> findAll(@PathVariable Long mascotaId) throws EntityNotFoundException {
        MascotaEntity mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new EntityNotFoundException("La mascota con el id dado no existe"));
        return mensajeRepository.findByMascotaId(mascota.getId()).stream()
                .map(entity -> modelMapper.map(entity, MensajeDTODetail.class))
                .collect(Collectors.toList());
    }
}
