package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDtoDetails;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.RosterRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final RosterRepository rosterRepository;
    private final EmployeeService employeeService;
    private final RosterService rosterService;

    public TeamService(TeamRepository teamRepository, EmployeeRepository employeeRepository, RosterRepository rosterRepository, EmployeeService employeeService, RosterService rosterService) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.rosterRepository = rosterRepository;
        this.employeeService = employeeService;

        this.rosterService = rosterService;
    }

    public Iterable<TeamOutputDto> getAllTeams() {
        List<TeamOutputDto> teamOutputDtoEmpIds = new ArrayList<>();
        for (Team team : teamRepository.findAll()) {
            teamOutputDtoEmpIds.add(transferTeamToTeamOutputDto(team));
        }
        return teamOutputDtoEmpIds;
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

    public TeamOutputDtoDetails getTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No Team found with name: " + id));
        return transferTeamToTeamOutputDtoDetails(team);
    }

    public TeamOutputDto createTeam(TeamInputDto teamInputDto) {
        if (teamRepository.existsById(teamInputDto.getTeamName())) {
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

        return teamOutputDto;
    }

    private TeamOutputDtoDetails transferTeamToTeamOutputDtoDetails(Team team) {
        TeamOutputDtoDetails teamOutputDtoDetails = new TeamOutputDtoDetails();
        teamOutputDtoDetails.setTeamName(team.getTeamName());
        teamOutputDtoDetails.setIsActive(team.getIsActive());

        teamOutputDtoDetails.setEmployeesOutputDtos(transferEmployeesToDtos(employeeRepository.findAllByTeam(team)));
        teamOutputDtoDetails.setRostersOutputDtos(tranferRostersToDtos(rosterRepository.findAllByTeam(team)));
//        teamOutputDtoDetails.setRostersOutputDtos(rosterRepository.findAllByTeam(team));

        return teamOutputDtoDetails;
    }

    private List<EmployeeOutputDto> transferEmployeesToDtos (List<Employee> employees) {
        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for(Employee employee: employees) {
            employeeOutputDtos.add(employeeService.transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    private List<RosterOutputDto> tranferRostersToDtos (List<Roster> rosters) {
        List<RosterOutputDto> rosterOutputDtos = new ArrayList<>();
        for(Roster roster: rosters) {
            rosterOutputDtos.add(rosterService.transferRosterToOutputDto(roster));
        }
        return rosterOutputDtos;
    }
}
