package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.services.RosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
