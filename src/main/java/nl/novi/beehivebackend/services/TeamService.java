package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.TeamInputDto;
import nl.novi.beehivebackend.dtos.output.TeamOutputDto;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
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
            teamOutputDtos.add(convertTeamToDto(team));
        }
        return teamOutputDtos;
    }

    public TeamOutputDto getTeam(String teamName) {
        Team team = teamRepository.findById(teamName).orElseThrow(() -> new RecordNotFoundException("No Team found with name: " + teamName));
        return convertTeamToDto(team);
    }

    public TeamOutputDto createTeam(TeamInputDto teamInputDto) {
        Team team = teamRepository.save(convertDtoToTeam(teamInputDto));
        return convertTeamToDto(team);
    }

    public TeamOutputDto updateTeam(String teamName, TeamInputDto teamInputDto) {
        teamRepository.findById(teamName).orElseThrow(()-> new RecordNotFoundException("No team found with id: " + teamName));
        Team team = convertDtoToTeam(teamInputDto);
        team.setTeamName(teamName);
        teamRepository.save(team);
        return convertTeamToDto(team);
    }

    public void deleteTeam(String teamName) {
        try {
            teamRepository.deleteById(teamName);
        } catch (Exception e) {
            throw new RecordNotFoundException("No team found with id: " + teamName);
        }
    }

    private TeamOutputDto convertTeamToDto(Team team) {
        return modelMapper.map(team, TeamOutputDto.class);
    }

    private Team convertDtoToTeam(TeamInputDto teamInputDto) {
        return modelMapper.map(teamInputDto, Team.class);
    }


}
