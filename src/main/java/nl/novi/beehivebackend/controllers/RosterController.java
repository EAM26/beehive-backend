package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.services.RosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/rosters")
public class RosterController {

    private final RosterService rosterService;

    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @GetMapping
    public ResponseEntity<Iterable<RosterOutputDto>> getAllRosters() {
        return new ResponseEntity<>(rosterService.getAllRosters(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createRoster(@RequestBody RosterInputDto rosterInputDto) {
        RosterOutputDto rosterOutputDto = rosterService.createRoster(rosterInputDto);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + rosterOutputDto.id).toUriString());
            return ResponseEntity.created(uri).body(rosterOutputDto);
    }
}
