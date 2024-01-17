package nl.novi.beehivebackend.controllers;

import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDtoEmpDetails;
import nl.novi.beehivebackend.dtos.output.TeamOutputDtoEmpIds;
import nl.novi.beehivebackend.services.TeamService;
import nl.novi.beehivebackend.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final ValidationUtil validationUtil;

    public TeamController(TeamService teamService, ValidationUtil validationUtil) {
        this.teamService = teamService;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    public ResponseEntity<Iterable<TeamOutputDtoEmpIds>> getAllTeams(@RequestParam(required = false) Boolean isActive) {
        if (isActive != null) {
            return new ResponseEntity<>(teamService.getAllTeams(isActive), HttpStatus.OK);
        }
            return new ResponseEntity<>(teamService.getAllTeams(), HttpStatus.OK);
    }

    @GetMapping("/{teamName}")
    public ResponseEntity<TeamOutputDtoEmpDetails> getTeam(@PathVariable String teamName) {
        return new ResponseEntity<>(teamService.getTeam(teamName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@Valid @RequestBody TeamInputDto teamInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        TeamOutputDtoEmpIds teamOutputDtoEmpIds = teamService.createTeam(teamInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + teamOutputDtoEmpIds.getTeamName()).toUriString());
        return ResponseEntity.created(uri).body(teamOutputDtoEmpIds.getTeamName() + " created");
    }

    @PutMapping("/{teamName}")
    public ResponseEntity<Object> updateTeam(@PathVariable String teamName, @Valid @RequestBody TeamInputDto teamInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        TeamOutputDtoEmpIds teamOutputDtoEmpIds = teamService.updateTeam(teamName, teamInputDto);
        return new ResponseEntity<>(teamOutputDtoEmpIds.getTeamName() + " updated.", HttpStatus.OK);
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteTeam(@PathVariable String teamName) {
//        teamService.deleteTeam(teamName);
//        return ResponseEntity.noContent().build();
//    }

}
