package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    public TeamService(TeamRepository teamRepository) {
        this.modelMapper = new ModelMapper();
        this.teamRepository = teamRepository;
    }

    public Iterable<TeamOutputDto> getAllTeams() {
        List<TeamOutputDto> teamOutputDtos = new ArrayList<>();
        for(Team team: teamRepository.findAll()) {
            teamOutputDtos.add(transferTeamToTeamOutputDto(team));
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

    public TeamOutputDto updateTeam(String id, TeamInputDto teamInputDto) {
        if (teamRepository.existsByTeamNameIgnoreCase(teamInputDto.getTeamName())) {
            throw new IsNotUniqueException("A team with that name already exists.");
        }
        Team team = teamRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No team found with id: " + id));
        transferTeamInputDtoToTeam(teamInputDto, team);
        teamRepository.save(team);
        return transferTeamToTeamOutputDto(team);
    }

    public void deleteTeam(String id) {
        Team team = teamRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No team found with id: " + id));
        if(!team.getEmployees().isEmpty()) {
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
        teamOutputDto.setEmployees(team.getEmployees());

        return teamOutputDto;
    }

    private TeamOutputDto convertTeamToDto(Team team) {
        return modelMapper.map(team, TeamOutputDto.class);
    }

    private Team convertDtoToTeam(TeamInputDto teamInputDto) {
        return modelMapper.map(teamInputDto, Team.class);
    }


}
