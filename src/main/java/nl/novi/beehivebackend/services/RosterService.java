package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.repositories.RosterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;

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

    public RosterOutputDto createRoster(RosterInputDto rosterInputDto) {
        Roster roster = rosterRepository.save(transferDtoToRoster(rosterInputDto));
        System.out.println("service create");
        return convertRosterToOutputDto(roster);
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
