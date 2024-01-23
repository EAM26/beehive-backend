package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
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

    @GetMapping("/{rosterName}")
    public ResponseEntity<RosterOutputDto> getRoster(@PathVariable String rosterName) {
        return new ResponseEntity<>(rosterService.getSingleRoster(rosterName), HttpStatus.OK);
    }

    @DeleteMapping("/{rosterName}")
    public ResponseEntity<HttpStatus> deleteRoster(@PathVariable String rosterName) {
        rosterService.deleteRoster(rosterName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
