package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.repositories.RosterRepository;
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
    private final ModelMapper modelMapper;

    public RosterService(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
        this.modelMapper = new ModelMapper();
    }

    public Iterable<RosterOutputDto> getAllRosters() {
        List<RosterOutputDto> rosterOutputDtos = new ArrayList<>();
        for (Roster roster: rosterRepository.findAll()) {
            rosterOutputDtos.add(convertRosterToOutputDto(roster));
        }
        return rosterOutputDtos;
    }

    public RosterOutputDto getRoster(String id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No roster found with id: " + id));
        return convertRosterToOutputDto(roster);
    }

    public RosterOutputDto createRoster(RosterInputDto rosterInputDto) {
        Roster roster = rosterRepository.save(transferDtoToRoster(rosterInputDto));
        System.out.println("service create");
        return convertRosterToOutputDto(roster);
    }

    // TODO: 11-7-2023 Check getShifts, methode geschreven voor relatie met Shift was gelegd
    public void deleteRoster(String id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No roster found with id " + id));
        if(!roster.getShifts().isEmpty()) {
            throw new IsNotEmptyException("Roster is not empty. First remove all shifts");
        }
        rosterRepository.deleteById(id);
    }


    private RosterOutputDto convertRosterToOutputDto(Roster roster) {
        return modelMapper.map(roster, RosterOutputDto.class);
    }


    private Roster transferDtoToRoster(RosterInputDto rosterInputDto) {
        Roster roster = new Roster();
        roster.setYear(rosterInputDto.getYear());
        roster.setWeekNumber(rosterInputDto.getWeekNumber());

        LocalDate firstDayOfYear = LocalDate.of(roster.getYear(), 1, 1);

        // Define the week-based year and week fields
        TemporalField weekBasedYearField = WeekFields.ISO.weekBasedYear();
        TemporalField weekOfWeekBasedYearField = WeekFields.ISO.weekOfWeekBasedYear();

        // Calculate the start and end dates of the week
        LocalDate startOfWeek = firstDayOfYear.with(weekBasedYearField, roster.getYear())
                .with(weekOfWeekBasedYearField, roster.getWeekNumber())
                .with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        roster.setStartOfWeek(startOfWeek);
        roster.setEndOfWeek(endOfWeek);
        roster.setId(roster.getWeekNumber() + "-" + roster.getYear());
        return roster;
    }



}