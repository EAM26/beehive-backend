package nl.novi.beehivebackend.controllers;

import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
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
    public ResponseEntity<Iterable<TeamOutputDto>> getAllTeams(@RequestParam(required = false) Boolean isActive) {
        if (isActive != null) {
            return new ResponseEntity<>(teamService.getAllTeams(isActive), HttpStatus.OK);
        }
            return new ResponseEntity<>(teamService.getAllTeams(), HttpStatus.OK);
    }

    @GetMapping("/{teamName}")
    public ResponseEntity<TeamOutputDto> getTeam(@PathVariable String teamName) {
        return new ResponseEntity<>(teamService.getTeam(teamName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@Valid @RequestBody TeamInputDto teamInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        TeamOutputDto teamOutputDto = teamService.createTeam(teamInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + teamOutputDto.getTeamName()).toUriString());
        return ResponseEntity.created(uri).body(teamOutputDto.getTeamName() + " created");
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateTeam(@PathVariable String teamName, @Valid @RequestBody TeamInputDto teamInputDto, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
//        }
//        return new ResponseEntity<>(teamService.updateTeam(teamName, teamInputDto), HttpStatus.ACCEPTED);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteTeam(@PathVariable String teamName) {
//        teamService.deleteTeam(teamName);
//        return ResponseEntity.noContent().build();
//    }

}
