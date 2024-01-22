package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Absence;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.AbsenceRepository;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ShiftRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final AbsenceRepository absenceRepository;


    public ShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, TeamRepository teamRepository, AbsenceRepository absenceRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.absenceRepository = absenceRepository;
    }

    public Iterable<ShiftOutputDto> getAllShifts() {
        ArrayList<ShiftOutputDto> shiftOutputDtos = new ArrayList<>();
        for (Shift shift : shiftRepository.findAll()) {
            shiftOutputDtos.add(transferShiftToShiftOutputDto(shift));
        }
        return shiftOutputDtos;
    }

    public ShiftOutputDto getShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        return transferShiftToShiftOutputDto(shift);
    }

    public ShiftOutputDto createShift(ShiftInputDto shiftInputDto) {
        Shift shift = shiftRepository.save(shiftCheckManager(shiftInputDto, new Shift(), null));
        return transferShiftToShiftOutputDto(shift);
    }

    public ShiftOutputDto updateShift(Long id, ShiftInputDto shiftInputDto) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));

        shift = shiftRepository.save(shiftCheckManager(shiftInputDto, shift, id));
        return transferShiftToShiftOutputDto(shift);
    }


    private Shift createShiftManager(ShiftInputDto shiftInputDto) {

//        shift check duration
        isValidShiftDuration(shiftInputDto);

//        shift check if team is valid
        Team team = teamRepository.findById(shiftInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name. Name is case sensitive!"));
        isTeamActive(team);

        Shift shift = new Shift();
//        if shiftInput has no employee, save as shift and return
        if (shiftInputDto.getEmployeeId() == null) {
            return transferShiftInputDtoToShift(shiftInputDto, team, null, shift);
        }

//        if shiftInput has employee
//        Get employee and check match team
        Employee employee = employeeRepository.findById(shiftInputDto.getEmployeeId()).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + shiftInputDto.getEmployeeId()));
        isEmployeeActive(employee);
        isMatchEmployeeTeam(employee, team);

//        Check absence overlap start and end shift for employee
        if (shiftToAbsenceOverlap(shiftInputDto.getStartShift(), employee) || shiftToAbsenceOverlap(shiftInputDto.getEndShift(), employee)) {
            throw new BadRequestException("Shift overlaps absence");
        }

//        Check shift overlap for employee
        if (shiftToShiftOverlap(shiftInputDto, employee)) {
            throw new BadRequestException("Shift overlaps other shift.");
        }
        return transferShiftInputDtoToShift(shiftInputDto, team, employee, shift);
    }

    private Shift shiftCheckManager(ShiftInputDto shiftInputDto, Shift shift, Long originalShiftId) {

//        Shift check duration
        isValidShiftDuration(shiftInputDto);

//        Team check
        Team team = teamRepository.findById(shiftInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name. Name is case sensitive!"));
        if (!team.getIsActive()) {
            throw new BadRequestException("This team is not active");
        }



//        Employee check exists and in right team
        if (shiftInputDto.getEmployeeId() == null) {
            return transferShiftInputDtoToShift(shiftInputDto, team, null, shift);
        }
        Employee employee = employeeRepository.findById(shiftInputDto.getEmployeeId()).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + shiftInputDto.getEmployeeId()));
        System.out.println("*********************");
        System.out.println(employee.getId());
        System.out.println("*********************");
        if (!isMatchEmployeeTeam(employee, team)) {
            throw new BadRequestException("Employee is not in team: " + team.getTeamName());

        }

        Employee originalEmployee = employeeRepository.findById(shift.getEmployee().getId()).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + shift.getEmployee().getId()));
//        Long empId = originalEmployee.getId();
//        Long empShiftId = shiftInputDto.getEmployeeId();
//        Boolean isSameId = null;
//        if(empId == empShiftId) {
//            isSameId = true;
//        } else {
//            isSameId = false;
//        }

        if (shiftInputDto.getEmployeeId() != originalEmployee.getId()) {
            System.out.println("*********************");
            System.out.println("id's are NOT similar");
            System.out.println("*********************");
            //        If old employee is not new employee:  check overlap shift or absence
            if (shiftToShiftOverlap(shiftInputDto, employee, null)) {
                throw new BadRequestException("Shift overlaps other shift.");
            }

            if (shiftToAbsenceOverlap(shiftInputDto.getStartShift(), employee) || shiftToAbsenceOverlap(shiftInputDto.getEndShift(), employee)) {
                throw new BadRequestException("Shift overlaps absence");
            }

        } else {
            if (shiftToShiftOverlap(shiftInputDto, employee, originalShiftId)) {
                throw new BadRequestException("Shift overlaps other shift.");
            }
        }

        return transferShiftInputDtoToShift(shiftInputDto, team, employee, shift);

    }

    //  Checks if Employee is in given team
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

    //    Checks if Shifts of employee overlaps with other shift of employee
    private Boolean shiftToShiftOverlap(ShiftInputDto shiftInputDto, Employee employee, Long orignalShiftId) {
        for (Shift shift : shiftRepository.findByEmployeeId(employee.getId())) {
            if (shift.getStartShift().isBefore(shiftInputDto.getEndShift()) &&
                    shift.getEndShift().isAfter(shiftInputDto.getStartShift()) && shift.getId() != orignalShiftId) {
                return true;
            }
        }
        return false;
    }

    private Boolean shiftToShiftOverlap(ShiftInputDto shiftInputDto, Employee employee) {
        for (Shift shift : shiftRepository.findByEmployeeId(employee.getId())) {
            if (shift.getStartShift().isBefore(shiftInputDto.getEndShift()) &&
                    shift.getEndShift().isAfter(shiftInputDto.getStartShift())) {
                return true;
            }
        }
        return false;
    }

    //    Checks if date of shift overlaps absences of employee
    private Boolean shiftToAbsenceOverlap(LocalDateTime shiftDateTime, Employee employee) {
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
        if (employee != null) {
            shift.setEmployee(employee);
        }


        return shiftRepository.save(shift);
    }

    private ShiftOutputDto transferShiftToShiftOutputDto(Shift shift) {
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
//
//    public ShiftOutputDto updateShift(Long id, ShiftInputDto shiftInputDto) {
//        Shift shift = shiftRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No shift found with id: " + id));
//        Roster roster = rosterValidation(shiftInputDto);
//        if(shiftInputDto.getEmployeeId() != null) {
//            Employee employee = employeeValidation(shiftInputDto, roster);
//            shift = shiftRepository.save(transferShiftInputDtoToShift(shift, shiftInputDto, employee, roster));
//        } else {
//            shift = shiftRepository.save(transferShiftInputDtoToShift(shift, shiftInputDto, null, roster));
//        }
//        return transferShiftToShiftOutputDto(shift);
//
//
//    }
//
//    public void deleteShift(Long id) {
//        try {
//            shiftRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new RecordNotFoundException("No shift found with id: " + id);
//        }
//    }

//    private boolean isDateInWeek(LocalDate date, int targetWeekNumber, int targetYear) {
//        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
//        int year = date.get(IsoFields.WEEK_BASED_YEAR);
//
//        return weekNumber == targetWeekNumber && year == targetYear;
//    }
//
//
//    private Roster rosterValidation(ShiftInputDto shiftInputDto) {
////        Check if roster exists
//        Roster roster = rosterRepository.findById(shiftInputDto.getRosterId()).orElseThrow(() -> new RecordNotFoundException("No roster found with id: " + shiftInputDto.getRosterId()));
////        Check startdate to week
//        if (!(isDateInWeek(shiftInputDto.getStartDate(), roster.getWeekNumber(), roster.getYear()))) {
//            throw new IllegalValueException("Date is not in this week");
//        }
//        return roster;
//    }
//
//    private Employee employeeValidation(ShiftInputDto shiftInputDto, Roster roster) {
//        Employee employee = employeeRepository.findById(shiftInputDto.getEmployeeId()).orElseThrow(() -> new RecordNotFoundException("This employee doesn't exist"));
//
////            Check is employed
//        if (!employee.getIsEmployed()) {
//            throw new IllegalValueException(employee.getShortName() + " is not employed");
//        }
//
////            Check is right team
//        if (!(roster.getTeam().getEmployees().contains(employee))) {
//            throw new RecordNotFoundException("Team " + roster.getTeam().getTeamName() + " doesn't have employee " + employee.getShortName());
//        }
//        return employee;
//    }

//    Overload transfer postmapping
//    private Shift transferShiftInputDtoToShift(ShiftInputDto shiftInputDto, Employee employee, Roster roster) {
//        Shift shift = new Shift();
//        return transferShiftInputDtoToShift(shift, shiftInputDto, employee, roster);
//    }


//     Overload transfer putmapping
//    private Shift transferShiftInputDtoToShift(Shift shift, ShiftInputDto shiftInputDto, Employee employee, Roster roster) {
//        shift.setStart(shiftInputDto.getStartDate());
//        shift.setEnd(shiftInputDto.getEndDate());
//        shift.setStartTime(shiftInputDto.getStartTime());
//        shift.setEndTime(shiftInputDto.getEndTime());
//        shift.setRoster(roster);
//        if (employee != null) {
//            shift.setEmployee(employee);
//        }
//        return shift;
//    }


}
