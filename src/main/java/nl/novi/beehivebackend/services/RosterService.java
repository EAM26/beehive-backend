package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.RosterRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Service
public class RosterService {

    private final RosterRepository rosterRepository;
    private final TeamRepository teamRepository;


    public RosterService(RosterRepository rosterRepository, TeamRepository teamRepository) {
        this.rosterRepository = rosterRepository;
        this.teamRepository = teamRepository;

    }

    public Iterable<RosterOutputDto> getAllRosters() {
        List<RosterOutputDto> rosterOutputDtos = new ArrayList<>();
        for (Roster roster: rosterRepository.findAll()) {
            rosterOutputDtos.add(transferRostertoRosterOutputDto(roster));
        }
        return rosterOutputDtos;
    }

    public RosterOutputDto getRoster(String id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No roster found with id: " + id));
        return transferRostertoRosterOutputDto(roster);
    }



//    public RosterOutputDto createRoster(RosterInputDto rosterInputDto) {
//        Roster roster = rosterRepository.save(transferRosterInputDtoToRoster(rosterInputDto));
//        return transferRostertoRosterOutputDto(roster);
//    }
//
//    // TODO: 11-7-2023 Check getShifts, methode geschreven voor relatie met Shift was gelegd
//    public void deleteRoster(String id) {
//        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No roster found with id " + id));
//        if(!roster.getShifts().isEmpty()) {
//            throw new IsNotEmptyException("Roster is not empty. First remove all shifts");
//        }
//        rosterRepository.deleteById(id);
//    }

    public RosterOutputDto transferRostertoRosterOutputDto(Roster roster) {
        RosterOutputDto rosterOutputDto = new RosterOutputDto();
        rosterOutputDto.setId(roster.getId());
        rosterOutputDto.setYear(roster.getYear());
        rosterOutputDto.setWeekNumber(roster.getWeekNumber());
        rosterOutputDto.setStartOfWeek(roster.getStartOfWeek());
        rosterOutputDto.setEndOfWeek(roster.getEndOfWeek());
        rosterOutputDto.setTeam(roster.getTeam());
        rosterOutputDto.setShifts(roster.getShifts());

        return rosterOutputDto;
    }

//    public Roster transferRosterInputDtoToRoster(RosterInputDto rosterInputDto) {
//        Roster roster = new Roster();
//        roster.setYear(rosterInputDto.getYear());
//        roster.setWeekNumber(rosterInputDto.getWeekNumber());
////       check if team exists
//        Team team = teamRepository.findById(rosterInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + rosterInputDto.getTeamId()));
//        String tempId = roster.getWeekNumber() + "-" + roster.getYear() + "-" + team.getTeamName();
//        if(rosterRepository.existsById(tempId)) {
//            throw new IsNotUniqueException("This roster already exists.");
//        }
//        roster.setId(tempId);
//
//        LocalDate firstDayOfYear = LocalDate.of(roster.getYear(), 1, 1);
//
//        // Define the week-based year and week fields
//        TemporalField weekBasedYearField = WeekFields.ISO.weekBasedYear();
//        TemporalField weekOfWeekBasedYearField = WeekFields.ISO.weekOfWeekBasedYear();
//
//        // Calculate the start and end dates of the week
//        roster.setStartOfWeek(firstDayOfYear.with(weekBasedYearField, roster.getYear())
//                .with(weekOfWeekBasedYearField, roster.getWeekNumber())
//                .with(java.time.DayOfWeek.MONDAY));
//        roster.setEndOfWeek(roster.getStartOfWeek().plusDays(6));
//
////        Get and set team
//        roster.setTeam(team);
//
//        return roster;
//    }

}
