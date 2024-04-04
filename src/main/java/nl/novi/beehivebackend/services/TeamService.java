package nl.novi.beehivebackend.services;

import jakarta.transaction.Transactional;
import nl.novi.beehivebackend.dtos.input.TeamInputDto;
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

    @Transactional
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
        List<Employee> employees = employeeRepository.findAllByTeam(team);
        List<String> empsData = new ArrayList<>();
        for(Employee employee: employees) {
            System.out.println(employee.getShortName());
            empsData.add(employee.getShortName()+ ": " + employee.getId());
        }
        teamOutputDtoDetails.setEmployeesData(empsData);
        List<String> rosterData = new ArrayList<>();
        for(Roster roster: rosterRepository.findAllByTeam(team)) {
            System.out.println(roster.getWeek() + "-" + roster.getYear() + "-" + roster.getTeam().getTeamName());
            rosterData.add(roster.getWeek() + "-" + roster.getYear() + "-" + roster.getTeam().getTeamName());
        }
        teamOutputDtoDetails.setRosterData(rosterData);
        return teamOutputDtoDetails;
    }



}
