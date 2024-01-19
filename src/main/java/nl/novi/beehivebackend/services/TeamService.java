package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDtoEmpDetails;
import nl.novi.beehivebackend.dtos.output.TeamOutputDtoEmpIds;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    public TeamService(TeamRepository teamRepository, EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
    }

    public Iterable<TeamOutputDtoEmpIds> getAllTeams() {
        List<TeamOutputDtoEmpIds> teamOutputDtoEmpIds = new ArrayList<>();
        for (Team team : teamRepository.findAll()) {
            teamOutputDtoEmpIds.add(transferTeamToTeamOutputDtoEmpIds(team));
        }
        return teamOutputDtoEmpIds;
    }

    public Iterable<TeamOutputDtoEmpIds> getAllTeams(Boolean isActive) {
        List<TeamOutputDtoEmpIds> teamOutputDtoEmpIds = new ArrayList<>();
        for (Team team : teamRepository.findAll()) {
            if (team.getIsActive() == isActive) {
                teamOutputDtoEmpIds.add(transferTeamToTeamOutputDtoEmpIds(team));
            }

        }
        return teamOutputDtoEmpIds;
    }

    public TeamOutputDtoEmpDetails getTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No Team found with name: " + id));
        return transferTeamToTeamOutputDtoEmpDetails(team);
    }

    public TeamOutputDtoEmpIds createTeam(TeamInputDto teamInputDto) {
        if (teamRepository.existsById(teamInputDto.getTeamName())) {
            throw new BadRequestException("A team with that name already exists.");
        }
        Team team = teamRepository.save(transferTeamInputDtoToTeam(teamInputDto));
        return transferTeamToTeamOutputDtoEmpIds(team);
    }

    public TeamOutputDtoEmpIds updateTeam(String teamName, TeamInputDto teamInputDto) {
        Team teamToUpdate = teamRepository.findById(teamName).orElseThrow(() -> new RecordNotFoundException("Team with name " + teamName + " doesn't exist."));
        if (!teamInputDto.getTeamName().equals(teamName)) {
            throw new BadRequestException("Not allowed to change the Team name.");
        }

        Team updatedTeam = transferTeamInputDtoToTeam(teamInputDto, teamToUpdate);
        Team team = teamRepository.save(updatedTeam);
        return transferTeamToTeamOutputDtoEmpIds(team);
    }

    public void deleteTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + id));
        if (!team.getEmployees().isEmpty()) {
            throw new IsNotEmptyException("Team is not empty. First remove all employees");
        }
        teamRepository.deleteById(id);
    }

//  HELPER METHODS

    // Overload transfer for postmapping
    private Team transferTeamInputDtoToTeam(TeamInputDto teamInputDto) {
        return transferTeamInputDtoToTeam(teamInputDto, new Team());
    }

    // Overload transfer for putmapping
    private Team transferTeamInputDtoToTeam(TeamInputDto teamInputDto, Team team) {
        team.setTeamName(teamInputDto.getTeamName());
        team.setIsActive(teamInputDto.getIsActive());
        return team;
    }

    private TeamOutputDtoEmpIds transferTeamToTeamOutputDtoEmpIds(Team team) {
        TeamOutputDtoEmpIds teamOutputDtoEmpIds = new TeamOutputDtoEmpIds();
        teamOutputDtoEmpIds.setTeamName(team.getTeamName());
        teamOutputDtoEmpIds.setIsActive(team.getIsActive());
        teamOutputDtoEmpIds.setEmployeeIds(employeeRepository.findAllIds());

        return teamOutputDtoEmpIds;
    }

    private TeamOutputDtoEmpDetails transferTeamToTeamOutputDtoEmpDetails(Team team) {
        TeamOutputDtoEmpDetails teamOutputDtoEmpDetails = new TeamOutputDtoEmpDetails();
        teamOutputDtoEmpDetails.setTeamName(team.getTeamName());
        teamOutputDtoEmpDetails.setIsActive(team.getIsActive());
        teamOutputDtoEmpDetails.setEmployees(employeeRepository.findAll());

        return teamOutputDtoEmpDetails;
    }
}
