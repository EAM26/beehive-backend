package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.modelmapper.ModelMapper;
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

    public Iterable<TeamOutputDto> getAllTeams() {
        List<TeamOutputDto> teamOutputDtos = new ArrayList<>();
        for (Team team : teamRepository.findAll()) {
            teamOutputDtos.add(transferTeamToTeamOutputDto(team));
        }
        return teamOutputDtos;
    }

    public Iterable<TeamOutputDto> getAllTeams(Boolean isActive) {
        List<TeamOutputDto> teamOutputDtos = new ArrayList<>();
        for (Team team : teamRepository.findAll()) {
            if (team.getIsActive() == isActive) {
                teamOutputDtos.add(transferTeamToTeamOutputDto(team));
            }

        }
        return teamOutputDtos;
    }

    public TeamOutputDto getTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No Team found with name: " + id));
        return transferTeamToTeamOutputDto(team);
    }

    public TeamOutputDto createTeam(TeamInputDto teamInputDto) {
        if (teamRepository.existsByTeamNameIgnoreCase(teamInputDto.getTeamName())) {
            throw new BadRequestException("A team with that name already exists.");
        }
        Team team = teamRepository.save(transferTeamInputDtoToTeam(teamInputDto));
        return transferTeamToTeamOutputDto(team);
    }

    public TeamOutputDto updateTeam(String teamName, TeamInputDto teamInputDto) {
        Team teamToUpdate = teamRepository.findById(teamName).orElseThrow(() -> new RecordNotFoundException("Team with name " + teamName + " doesn't exist."));
        if (!teamInputDto.getTeamName().equals(teamName)) {
            throw new BadRequestException("Not allowed to change the Team name.");
        }

        Team updatedTeam = transferTeamInputDtoToTeam(teamInputDto, teamToUpdate);
        Team team = teamRepository.save(updatedTeam);
        return transferTeamToTeamOutputDto(team);
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

    private TeamOutputDto transferTeamToTeamOutputDto(Team team) {
        TeamOutputDto teamOutputDto = new TeamOutputDto();
        teamOutputDto.setTeamName(team.getTeamName());
        teamOutputDto.setIsActive(team.getIsActive());
        teamOutputDto.setEmployeeIds(employeeRepository.findAllIds());

        return teamOutputDto;
    }
}
