package nl.novi.beehivebackend.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.dtos.output.RosterNameOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.utils.ValidationUtil;
import nl.novi.beehivebackend.services.RosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/rosters")
public class RosterController {

    private final RosterService rosterService;
    private final ValidationUtil validationUtil;

    public RosterController(RosterService rosterService, ValidationUtil validationUtil) {
        this.rosterService = rosterService;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    public ResponseEntity<Iterable<RosterNameOutputDto>> getAllRosters(@RequestParam(required = false)String  teamName) {
        if(teamName != null) {
            return new ResponseEntity<>(rosterService.getAllRosters(teamName), HttpStatus.OK);
        }
        return new ResponseEntity<>(rosterService.getAllRosters(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<RosterOutputDto> getRoster(@PathVariable Long id) {
        return new ResponseEntity<>(rosterService.getSingleRoster(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createRoster(@Valid @RequestBody RosterInputDto rosterInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        RosterOutputDto rosterOutputDto = rosterService.createRoster(rosterInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + rosterOutputDto.getId()).toUriString());
        return ResponseEntity.created(uri).body("Roster created id: " + rosterOutputDto.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRoster(@PathVariable Long id) {
        rosterService.deleteRoster(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
