package nl.novi.beehivebackend.services;

import jakarta.transaction.Transactional;
import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.*;
import nl.novi.beehivebackend.repositories.*;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final AbsenceRepository absenceRepository;
    private final RosterRepository rosterRepository;


    public ShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, TeamRepository teamRepository, AbsenceRepository absenceRepository, RosterRepository rosterRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.absenceRepository = absenceRepository;
        this.rosterRepository = rosterRepository;
    }

    @Transactional
    public Iterable<ShiftOutputDto> getAllShifts() {
        ArrayList<ShiftOutputDto> shiftOutputDtos = new ArrayList<>();
        for (Shift shift : shiftRepository.findAll()) {
            shiftOutputDtos.add(transferShiftToShiftOutputDto(shift));
        }
        return shiftOutputDtos;
    }

    @Transactional
    public Iterable<ShiftOutputDto> getAllShiftsByRoster(Long id) {
        ArrayList<ShiftOutputDto> shiftOutputDtos = new ArrayList<>();
        for (Shift shift : shiftRepository.findByRosterId(id)) {
            shiftOutputDtos.add(transferShiftToShiftOutputDto(shift));
        }
        return shiftOutputDtos;
    }

    @Transactional
    public ShiftOutputDto getShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        return transferShiftToShiftOutputDto(shift);
    }

    public ShiftOutputDto createShift(ShiftInputDto shiftInputDto) {
        Shift shift = shiftRepository.save(shiftManager(shiftInputDto, new Shift()));
        return transferShiftToShiftOutputDto(shift);
    }

    public ShiftOutputDto updateShift(Long id, ShiftInputDto shiftInputDto) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));

        shift = shiftRepository.save(shiftManager(shiftInputDto, shift));
        return transferShiftToShiftOutputDto(shift);
    }

    public void deleteShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        shiftRepository.delete(shift);
    }


    private Shift shiftManager(ShiftInputDto shiftInputDto, Shift shift) {
//        check shift duration
        isValidShiftDuration(shiftInputDto);

//        shift check if team is valid
        Team team = teamRepository.findById(shiftInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with that name. Name is case sensitive!"));
        isTeamActive(team);

//        if shiftInput has no employee, save as shift and return
        if (shiftInputDto.getEmployeeId() == null) {
            return transferShiftInputDtoToShift(shiftInputDto, team, null, shift);
        }

//        if shift has employee
//        Get employee and check match team
        Employee employee = employeeRepository.findById(shiftInputDto.getEmployeeId()).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + shiftInputDto.getEmployeeId()));
        isEmployeeActive(employee);
        if (!isMatchEmployeeTeam(employee, team)) {
            throw new BadRequestException("Employee and team are no match");
        }

//        Check absence overlap start and end shift for employee
        if (shiftToAbsenceOverlap(shiftInputDto.getStartShift(), employee) || shiftToAbsenceOverlap(shiftInputDto.getEndShift(), employee)) {
            throw new BadRequestException("Shift overlaps absence");
        }

        if (shiftToShiftOverlap(shiftInputDto, employee, shift)) {
            throw new BadRequestException("Shift overlaps other shift.");
        }
        return transferShiftInputDtoToShift(shiftInputDto, team, employee, shift);

    }


    //   Get week and year from startShift for rosterName
    private int extractWeekNumber(ShiftInputDto shiftInputDto) {
        WeekFields weekFields = WeekFields.ISO;
        return shiftInputDto.getStartShift().get(weekFields.weekOfWeekBasedYear());
    }

    private int extractYear(ShiftInputDto shiftInputDto) {
        WeekFields weekFields = WeekFields.ISO;
        return shiftInputDto.getStartShift().get(weekFields.weekBasedYear());
    }


    private Boolean isMatchEmployeeTeam(Employee employee, Team team) {
        return employee.getTeam().getTeamName().equals(team.getTeamName());
    }

    private Boolean isTeamActive(Team team) {
        if (!team.getIsActive()) {
            throw new BadRequestException("Team  not active");
        }
        return true;
    }

    private Boolean isEmployeeActive(Employee employee) {
        if (!employee.getIsActive()) {
            throw new BadRequestException("Employee  not active");
        }
        return true;
    }


    private Boolean shiftToShiftOverlap(ShiftInputDto shiftInputDto, Employee employee, Shift shift) {
        for (Shift plannedShift : shiftRepository.findByEmployeeId(employee.getId())) {
//            No shift check against itself
            if (plannedShift.getStartShift().isBefore(shiftInputDto.getEndShift()) &&
                    plannedShift.getEndShift().isAfter(shiftInputDto.getStartShift()) && !Objects.equals(plannedShift.getId(), shift.getId())) {
                return true;
            }
        }
        return false;
    }

    //   Checks if date of shift overlaps absences of employee
    public Boolean shiftToAbsenceOverlap(LocalDateTime shiftDateTime, Employee employee) {
        LocalDate shiftDate = shiftDateTime.toLocalDate();
        for (Absence absence : absenceRepository.findByEmployeeId(employee.getId())) {
            if (!shiftDate.isBefore(absence.getStartDate()) && !shiftDate.isAfter(absence.getEndDate())) {
                return true;
            }
        }
        return false;
    }


    //    Checks if shift < 24h and end of shift is after start
    public boolean isValidShiftDuration(ShiftInputDto shiftInputDto) {
        if (shiftInputDto.getStartShift() == null || shiftInputDto.getEndShift() == null) {
            throw new BadRequestException("Start and End of shift are required");
        }

        // Check if endShift is after startShift
        if (!shiftInputDto.getEndShift().isAfter(shiftInputDto.getStartShift())) {
            throw new BadRequestException("End of shift not after start of shift.");
        }

        Duration duration = Duration.between(shiftInputDto.getStartShift(), shiftInputDto.getEndShift());
        if (!(duration.toHours() <= 24)) {
            throw new BadRequestException("Duration of shift is longer than 24 hours");
        }
        return true;
    }

    private Shift transferShiftInputDtoToShift(ShiftInputDto shiftInputDto, Team team, Employee employee, Shift shift) {
        shift.setStartShift(shiftInputDto.getStartShift());
        shift.setEndShift(shiftInputDto.getEndShift());
        shift.setTeam(team);
        shift.setEmployee(employee);

//        if (employee != null) {
//            shift.setEmployee(employee);
//        }

        shift.setWeekNumber(extractWeekNumber(shiftInputDto));
        shift.setYear(extractYear(shiftInputDto));
        Roster roster = rosterManager(shift);
        rosterRepository.save(roster);
        shift.setRoster(roster);

        return shiftRepository.save(shift);
    }

    private Roster rosterManager(Shift shift) {
        int weekNumber = shift.getWeekNumber();
        int year = shift.getYear();
        Team team = shift.getTeam();
        for (Roster roster : rosterRepository.findAll()) {
            if (weekNumber == roster.getWeek() && year == roster.getYear() && team.getTeamName().equals(roster.getTeam().getTeamName())) {
                return roster;
            }
        }
        throw new BadRequestException("No roster found for " + weekNumber + "-" + year + "-" + team.getTeamName());

    }

    public ShiftOutputDto transferShiftToShiftOutputDto(Shift shift) {
        ShiftOutputDto shiftOutputDto = new ShiftOutputDto();
        shiftOutputDto.setId(shift.getId());
        shiftOutputDto.setStartShift(shift.getStartShift());
        shiftOutputDto.setEndShift(shift.getEndShift());
        if (shift.getEmployee() != null) {
            shiftOutputDto.setEmployeeShortName(shift.getEmployee().getShortName());
        }
        shiftOutputDto.setTeamName(shift.getTeam().getTeamName());

        return shiftOutputDto;
    }


}
