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

    public TeamOutputDto getTeam(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No Team found with id: " + id));
        return convertTeamToDto(team);
    }

    public TeamOutputDto createTeam(TeamInputDto teamInputDto) {
        Team team = teamRepository.save(convertDtoToTeam(teamInputDto));
        return convertTeamToDto(team);
    }

    public TeamOutputDto updateTeam(Long id, TeamInputDto teamInputDto) {
        teamRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No team found with id: " + id));
        Team team = convertDtoToTeam(teamInputDto);
        team.setId(id);
        teamRepository.save(team);
        return convertTeamToDto(team);
    }

    public void deleteTeam(Long id) {
        try {
            teamRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("No team found with id: " + id);
        }
    }

    private TeamOutputDto convertTeamToDto(Team team) {
        return modelMapper.map(team, TeamOutputDto.class);
    }

    private Team convertDtoToTeam(TeamInputDto teamInputDto) {
        return modelMapper.map(teamInputDto, Team.class);
    }


}
