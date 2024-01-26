package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.*;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
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


    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;

    }


    public Iterable<EmployeeOutputDto> getAllEmployees() {
        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll(Sort.by("id"))) {
            employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public Iterable<EmployeeOutputDto> getAllEmployees(String teamName) {
        Team team = teamRepository.findById(teamName).orElseThrow(() -> new BadRequestException("No team with name: " + teamName));

        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee : employeeRepository.findAllByTeam(team, Sort.by("id"))) {
            employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
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



    private User getUser(EmployeeInputDto employeeInputDto) {
        return userRepository.findById(employeeInputDto.getUsername()).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + employeeInputDto.getUsername()));
    }

    private Team getTeam(EmployeeInputDto employeeInputDto) {
        return teamRepository.findById(employeeInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + employeeInputDto.getTeamName()));
    }


//    public EmployeeOutputDto updateEmployee(Long id, EmployeeInputDto employeeInputDto) {
//        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
//        if (!employee.getShortName().equalsIgnoreCase(employeeInputDto.getShortName())) {
//            if (employeeRepository.existsByShortNameIgnoreCase(employeeInputDto.getShortName())) {
//                throw new IsNotUniqueException("Short name already exists.");
//            }
//        }
//
////        check if team exists
//        Team team = teamRepository.findById(employeeInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + employeeInputDto.getTeamName()));
//
//
//        transferEmployeeInputDtoToEmployee(employeeInputDto, employee, user);
//        employeeRepository.save(employee);
//        return transferEmployeeToEmployeeOutputDto(employee);
//    }

//    public void deleteEmployee(Long id) {
//        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id " + id));
////        if (!employee.getShifts().isEmpty()) {
////            throw new IsNotEmptyException("Employee is not empty. First remove all shifts");
////        }
//        employeeRepository.deleteById(id);
//    }


    private EmployeeOutputDto transferEmployeeToEmployeeOutputDto(Employee employee) {
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

//    Overload transfer for postmapping

//    private Employee transferEmployeeInputDtoToEmployee(EmployeeInputDto employeeInputDto, Employee employee) {
//
//        transferEmployeeInputDtoToEmployee(employeeInputDto, employee, team);
//        return employee;
//    }


    // Overload transfer for putmapping
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
