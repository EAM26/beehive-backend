package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.dtos.output.RosterNameOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.services.RosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rosters")
public class RosterController {

    private final RosterService rosterService;

    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @GetMapping
    public ResponseEntity<Iterable<RosterNameOutputDto>> getAllRosters(@RequestParam(required = false)String  teamName) {
        if(teamName != null) {
            return new ResponseEntity<>(rosterService.getAllRosters(teamName), HttpStatus.OK);
        }
        return new ResponseEntity<>(rosterService.getAllRosters(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RosterOutputDto> getRoster(@PathVariable Long id) {
        return new ResponseEntity<>(rosterService.getSingleRoster(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRoster(@PathVariable Long id) {
        rosterService.deleteRoster(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
