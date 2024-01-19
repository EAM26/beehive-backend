package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.IllegalValueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ShiftRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;


    public ShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, TeamRepository teamRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
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

//    public ShiftOutputDto createShift(ShiftInputDto shiftInputDto) {
//        Roster roster = rosterValidation(shiftInputDto);
//        Shift shift;
//        if(shiftInputDto.getEmployeeId() != null) {
//            Employee employee = employeeValidation(shiftInputDto, roster);
//            shift = shiftRepository.save(transferShiftInputDtoToShift(shiftInputDto, employee, roster));
//        } else {
//            shift = shiftRepository.save(transferShiftInputDtoToShift(shiftInputDto, null, roster));
//        }
//        return transferShiftToShiftOutputDto(shift);
//
//    }
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

    private ShiftOutputDto transferShiftToShiftOutputDto(Shift shift) {
        ShiftOutputDto shiftOutputDto = new ShiftOutputDto();
        shiftOutputDto.setId(shift.getId());
        shiftOutputDto.setStartShift(shift.getStartShift());
        shiftOutputDto.setEndShift(shift.getEndShift());
        shiftOutputDto.setEmployeeShortName(shift.getEmployee().getShortName());
        shiftOutputDto.setTeamName(shift.getTeam().getTeamName());

        return shiftOutputDto;
    }

}
