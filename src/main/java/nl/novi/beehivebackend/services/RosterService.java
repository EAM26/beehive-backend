package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.output.RosterNameOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.repositories.RosterRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class RosterService {

    private final RosterRepository rosterRepository;

    public RosterService(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    public Iterable<RosterNameOutputDto> getAllRosters() {
        List<RosterNameOutputDto> rosters = new ArrayList<>();
        for(Roster roster: rosterRepository.findAll(Sort.by("id"))){
            rosters.add(transferRosterNameToDto(roster));
        }
        return rosters;
    }

    public RosterOutputDto getSingleRoster(Long id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new BadRequestException("No Roster found with id: " + id));

        return transferRosterToOutputDto(roster);
    }

    public void deleteRoster(Long id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new BadRequestException("No roster found with id: " + id));
        if (roster.getShifts() != null && !roster.getShifts().isEmpty()) {
            throw new BadRequestException("Roster has Shifts");
        }
        rosterRepository.delete(roster);
    }

    private List<LocalDate> getDatesOfWeek(int weekNumber, int year) {
//        String[] partsName = rosterName.split("-");
//        int weekNumber = Integer.parseInt(partsName[0]);
//        int year = Integer.parseInt(partsName[1]);

//        Get first day of year and adjust to weekNumber and year of rosterName
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1)
                .with(WeekFields.of(Locale.getDefault()).weekOfYear(), weekNumber)
                .with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()));

        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }

        return weekDates;

    }

    private RosterOutputDto transferRosterToOutputDto(Roster roster) {
        RosterOutputDto rosterOutputDto = new RosterOutputDto();
        rosterOutputDto.setName(roster.createRosterName());
        rosterOutputDto.setShifts(roster.getShifts());
        rosterOutputDto.setWeekDates(getDatesOfWeek(roster.getWeek(), roster.getYear()));
        return rosterOutputDto;
    }
    private RosterNameOutputDto transferRosterNameToDto(Roster roster) {
        RosterNameOutputDto rosterDto = new RosterNameOutputDto();
        rosterDto.setName(roster.createRosterName());

        return rosterDto;
    }
}
