package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.*;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ShiftRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import nl.novi.beehivebackend.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;


    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository, UserRepository userRepository, ShiftRepository shiftRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.shiftRepository = shiftRepository;
    }


    public Iterable<EmployeeOutputDto> getAllEmployees() {
        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll(Sort.by("id"))) {
            employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public EmployeeOutputDto getSingleEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No employee found with id: " + id));
        return transferEmployeeToEmployeeOutputDto(employee);
    }

    public Iterable<EmployeeOutputDto> getAllEmployees(String teamName) {
        Team team = teamRepository.findById(teamName).orElseThrow(() -> new BadRequestException("No team with name: " + teamName));

        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee : employeeRepository.findAllByTeam(team, Sort.by("id"))) {
            employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public Iterable<EmployeeOutputDto>getAvailableEmployees(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("No shift found with id: " + id));

        List<EmployeeOutputDto> availableEmployees = new ArrayList<>();
        for (Employee employee : employeeRepository.findAllByTeam(shift.getTeam())) {
            if(!isOverlapShift(shift, employee)) {
                availableEmployees.add(transferEmployeeForRoster(employee));
            }
        }
        return availableEmployees ;
    }

    private boolean isOverlapShift(Shift shift, Employee employee) {
        List<Shift> employeeShifts = shiftRepository.findByEmployeeId(employee.getId());
        if (employeeShifts.isEmpty()) {
            return false;
        }
        for (Shift plannedShift : employeeShifts) {
            if (plannedShift.getStartShift().isBefore(shift.getEndShift()) &&
                    plannedShift.getEndShift().isAfter(shift.getStartShift())) {
                return true;
            }

        }
        return false;
    }
    public EmployeeOutputDto createEmployee(EmployeeInputDto employeeInputDto) {

//        get user
        User user = getUser(employeeInputDto);
        if(user.getEmployee() != null) {
            throw new BadRequestException("User already has employee id.");
        }
//        check if short name is unique
        if (employeeRepository.existsByShortNameIgnoreCase(employeeInputDto.getShortName())) {
            throw new IsNotUniqueException("Short name already exists.");
        }

//        get Team
        Team team = getTeam(employeeInputDto);


        Employee employee = new Employee();
        employeeRepository.save(transferEmployeeInputDtoToEmployee(employeeInputDto, employee, user, team));
        return transferEmployeeToEmployeeOutputDto(employee);
    }

    public EmployeeOutputDto updateEmployee(EmployeeInputDto employeeInputDto, Long empId) {
        Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + empId) );

        User orignalUser = employee.getUser();
        User userToTest = getUser(employeeInputDto);

//        if user is changed, check is user already has employee
        if(orignalUser != userToTest && userToTest.getEmployee()!= null) {
            throw new BadRequestException("User already has employee id.");
        }

//        if original shortname is not equal to new shortname, check for unique
        if(!employee.getShortName().equalsIgnoreCase(employeeInputDto.getShortName())) {
            if (employeeRepository.existsByShortNameIgnoreCase(employeeInputDto.getShortName())) {
                throw new IsNotUniqueException("Short name already exists.");
            }
        }

//        Team can not change
        if(!employee.getTeam().getTeamName().equals(employeeInputDto.getTeamName())) {
            throw new BadRequestException("Not allowed to change team");
        }

        employeeRepository.save(transferEmployeeInputDtoToEmployee(employeeInputDto, employee, orignalUser, employee.getTeam()));
        return transferEmployeeToEmployeeOutputDto(employee);
    }



    private User getUser(EmployeeInputDto employeeInputDto) {
        return userRepository.findById(employeeInputDto.getUsername()).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + employeeInputDto.getUsername()));
    }


    private Team getTeam(EmployeeInputDto employeeInputDto) {
        return teamRepository.findById(employeeInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + employeeInputDto.getTeamName()));
    }



    public EmployeeOutputDto transferEmployeeToEmployeeOutputDto(Employee employee) {
        EmployeeOutputDto employeeOutputDto = new EmployeeOutputDto();
        employeeOutputDto.setId(employee.getId());
        employeeOutputDto.setFirstName(employee.getFirstName());
        employeeOutputDto.setPreposition(employee.getPreposition());
        employeeOutputDto.setLastName(employee.getLastName());
        employeeOutputDto.setShortName(employee.getShortName());
        employeeOutputDto.setDob(employee.getDob());
        employeeOutputDto.setPhoneNumber(employee.getPhoneNumber());
        employeeOutputDto.setIsActive(employee.getIsActive());
        employeeOutputDto.setTeam(employee.getTeam());
        employeeOutputDto.setShifts(shiftSorter(employee.getShifts()));
        employeeOutputDto.setAbsences(absenceSorter(employee.getAbsences()));
        employeeOutputDto.setUsername(employee.getUser().getUsername());

        return employeeOutputDto;
    }
    public EmployeeOutputDto transferEmployeeForRoster(Employee employee) {
        EmployeeOutputDto employeeOutputDto = new EmployeeOutputDto();
        employeeOutputDto.setId(employee.getId());
        employeeOutputDto.setFirstName(employee.getFirstName());
        employeeOutputDto.setPreposition(employee.getPreposition());
        employeeOutputDto.setLastName(employee.getLastName());
        employeeOutputDto.setShortName(employee.getShortName());
        employeeOutputDto.setDob(employee.getDob());
        employeeOutputDto.setPhoneNumber(employee.getPhoneNumber());
        employeeOutputDto.setIsActive(employee.getIsActive());
        employeeOutputDto.setUsername(employee.getUser().getUsername());

        return employeeOutputDto;
    }

    private List<Shift> shiftSorter(List<Shift> shifts) {
        if (shifts != null) {
            shifts.sort(Comparator.comparing(Shift::getStartShift));
        }
        return shifts;
    }

    private List<Absence> absenceSorter(List<Absence> absences) {
        if (absences != null) {
            absences.sort(Comparator.comparing(Absence::getStartDate));
        }
        return absences;
    }


    private Employee transferEmployeeInputDtoToEmployee(EmployeeInputDto employeeInputDto, Employee employee, User user, Team team) {
        employee.setFirstName(employeeInputDto.getFirstName());
        employee.setPreposition(employeeInputDto.getPreposition());
        employee.setLastName(employeeInputDto.getLastName());
        employee.setShortName(employeeInputDto.getShortName());
        employee.setDob(employeeInputDto.getDob());
        employee.setPhoneNumber(employeeInputDto.getPhoneNumber());
        employee.setIsActive(employeeInputDto.getIsActive());
        employee.setTeam(team);
        employee.setUser(user);
        user.setEmployee(employee);
        return employee;
    }


}
