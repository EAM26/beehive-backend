package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ShiftRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;


    public ShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, TeamRepository teamRepository, TeamRepository teamRepository1) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository1;
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
        Shift shift = new Shift();
//        Team check
        Team team = teamRepository.findById(shiftInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name. Name is case sensitive!"));
        if (!team.getIsActive()) {
            throw new BadRequestException("This team is not active");
        }

//        Employee check
        if(shiftInputDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(shiftInputDto.getEmployeeId()).orElseThrow(() -> new RecordNotFoundException("No employee found with name."));
            if(checkEmployeeMatchTeam(employee, team)) {
                shift = transferShiftInputDtoToShift(shiftInputDto, team, employee);
            }
        } else {
            shift = transferShiftInputDtoToShift(shiftInputDto, team, null);
        }
        shiftRepository.save(shift);


        return transferShiftToShiftOutputDto(shift);
    }


    private Boolean checkEmployeeMatchTeam(Employee employee, Team team) {
            if (!employee.getTeam().getTeamName().equals(team.getTeamName())) {
                throw new BadRequestException("Employee is not in team: " + team.getTeamName());
            }
            return true;
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

    private Shift transferShiftInputDtoToShift(ShiftInputDto shiftInputDto, Team team, Employee employee) {
        Shift shift = new Shift();
        shift.setStartShift(shiftInputDto.getStartShift());
        shift.setEndShift(shiftInputDto.getEndShift());
        shift.setTeam(team);
        if(employee!=null) {
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

}
