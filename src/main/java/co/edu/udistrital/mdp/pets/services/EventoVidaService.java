package co.edu.udistrital.mdp.pets.services;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.repositories.EventoVidaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
public class EventoVidaService {
    private final EventoVidaRepository eventoVidaRepository;

    EventoVidaService(EventoVidaRepository eventoVidaRepository) {
        this.eventoVidaRepository = eventoVidaRepository;
    }


}
