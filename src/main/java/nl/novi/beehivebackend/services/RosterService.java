package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.RosterInputDto;
import nl.novi.beehivebackend.dtos.output.RosterNameOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.RosterRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
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
    private final TeamRepository teamRepository;
    private final ShiftService shiftService;

    public RosterService(RosterRepository rosterRepository, TeamRepository teamRepository, ShiftService shiftService) {
        this.rosterRepository = rosterRepository;
        this.teamRepository = teamRepository;
        this.shiftService = shiftService;
    }

    public Iterable<RosterNameOutputDto> getAllRosters() {
        List<RosterNameOutputDto> rosters = new ArrayList<>();
        for (Roster roster : rosterRepository.findAll(Sort.by("id"))) {
            rosters.add(transferRosterNameToDto(roster));
        }
        return rosters;
    }

    public Iterable<RosterNameOutputDto> getAllRosters(String teamName) {
        Team team = teamRepository.findById(teamName).orElseThrow(() -> new BadRequestException("No team found with name: " + teamName));
        List<RosterNameOutputDto> rosters = new ArrayList<>();
        for (Roster roster : rosterRepository.findAllByTeam(team, Sort.by("id"))) {
            rosters.add(transferRosterNameToDto(roster));
        }
        return rosters;
    }

    public RosterOutputDto getSingleRoster(Long id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new BadRequestException("No Roster found with id: " + id));

        return transferRosterToOutputDto(roster);
    }

    public RosterOutputDto createRoster(RosterInputDto rosterInputDto) {
        Roster roster = rosterRepository.save(transferInputDtoToRoster(rosterInputDto));
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
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1)
                .with(WeekFields.of(Locale.getDefault()).weekOfYear(), weekNumber)
                .with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()));
        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }

        return weekDates;

    }

    private Roster transferInputDtoToRoster(RosterInputDto rosterInputDto) {
        Roster roster = new Roster();
        Team team = teamRepository.findById(rosterInputDto.getTeamName()).orElseThrow(()->new BadRequestException("No team found with that name."));

        if (!isValidWeek(rosterInputDto.getWeek(), rosterInputDto.getYear())) {
            throw new BadRequestException("Week and year combination doesn't exist.");

        }

        if(rosterExists(rosterInputDto, team)) {
            throw new BadRequestException("Roster already exists");
        }

        roster.setWeek(rosterInputDto.getWeek());
        roster.setYear(rosterInputDto.getYear());
        roster.setTeam(team);

        return roster;
    }

    private boolean rosterExists(RosterInputDto rosterInputDto, Team team) {
        for(Roster roster: rosterRepository.findAll()) {
            if(roster.getYear() == rosterInputDto.getYear() && roster.getWeek() == rosterInputDto.getWeek() && roster.getTeam() == team)  {
                return true;
            }
        }
        return false;
    }
    private boolean isValidWeek(int weekNumber, int year) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        int lastWeekOfYear = endOfYear.get(weekFields.weekOfWeekBasedYear());

//        Check if last day of year is not in week 1 of next year
        if (lastWeekOfYear == 1) {
            lastWeekOfYear = endOfYear.minusWeeks(1).get(weekFields.weekOfWeekBasedYear());
        }

        return weekNumber >= 1 && weekNumber <= lastWeekOfYear;
    }

    public RosterOutputDto transferRosterToOutputDto(Roster roster) {
        RosterOutputDto rosterOutputDto = new RosterOutputDto();
        List<ShiftOutputDto> shiftOutputDtos = new ArrayList<>();
        rosterOutputDto.setId(roster.getId());
        rosterOutputDto.setName(roster.createRosterName());
        rosterOutputDto.setTeamName(roster.getTeam().getTeamName());
        for(Shift shift: roster.getShifts()) {
            shiftOutputDtos.add(shiftService.transferShiftToShiftOutputDto(shift));
        }
        rosterOutputDto.setShiftOutputDtos(shiftOutputDtos);
        rosterOutputDto.setWeekDates(getDatesOfWeek(roster.getWeek(), roster.getYear()));
        return rosterOutputDto;
    }

    private RosterNameOutputDto transferRosterNameToDto(Roster roster) {
        RosterNameOutputDto rosterDto = new RosterNameOutputDto();
        rosterDto.setName(roster.createRosterName());
        rosterDto.setTeamName(roster.getTeam().getTeamName());
        rosterDto.setId(roster.getId());

        return rosterDto;
    }
}
