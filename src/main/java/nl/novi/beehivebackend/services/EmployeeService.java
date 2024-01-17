package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.IsNotUniqueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import nl.novi.beehivebackend.repositories.UserRepository;
import nl.novi.beehivebackend.utils.UserData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserData userData;

    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository, UserRepository userRepository, UserData userData) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.userData = userData;
    }


    public Iterable<EmployeeOutputDto> getAllEmployees() {
        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll()) {
            employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public Iterable<EmployeeOutputDto> getAllEmployees(boolean isEmployed) {
        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll()) {
            if (isEmployed == employee.getIsEmployed()) {
                employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
            }
        }
        return employeeOutputDtos;
    }

    public EmployeeOutputDto getSingleEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        return transferEmployeeToEmployeeOutputDto(employee);
    }

    public EmployeeOutputDto getOwnProfile() {
        try {
            Long empId = userData.getLoggedInUser().getEmployeeId();
            return getSingleEmployee(empId);
        } catch (Exception exception) {
            throw new RecordNotFoundException("No employee found with that username");
        }
    }

    public EmployeeOutputDto createEmployee(EmployeeInputDto employeeInputDto) {
        if (employeeRepository.existsByShortNameIgnoreCase(employeeInputDto.getShortName())) {
            throw new IsNotUniqueException("Short name already exists.");
        }
//        check if team exists
        Team team = teamRepository.findById(employeeInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + employeeInputDto.getTeamName()));

        Employee employee = employeeRepository.save(transferEmployeeInputDtoToEmployee(employeeInputDto, team));
        return transferEmployeeToEmployeeOutputDto(employee);
    }

    public EmployeeOutputDto updateEmployee(Long id, EmployeeInputDto employeeInputDto) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        if (!employee.getShortName().equalsIgnoreCase(employeeInputDto.getShortName())) {
            if (employeeRepository.existsByShortNameIgnoreCase(employeeInputDto.getShortName())) {
                throw new IsNotUniqueException("Short name already exists.");
            }
        }

//        check if team exists
        Team team = teamRepository.findById(employeeInputDto.getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with id: " + employeeInputDto.getTeamName()));


        transferEmployeeInputDtoToEmployee(employeeInputDto, employee, team);
        employeeRepository.save(employee);
        return transferEmployeeToEmployeeOutputDto(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id " + id));
        if (!employee.getShifts().isEmpty()) {
            throw new IsNotEmptyException("Employee is not empty. First remove all shifts");
        }
        employeeRepository.deleteById(id);
    }


    private EmployeeOutputDto transferEmployeeToEmployeeOutputDto(Employee employee) {
        EmployeeOutputDto employeeOutputDto = new EmployeeOutputDto();
        employeeOutputDto.setId(employee.getId());
        employeeOutputDto.setFirstName(employee.getFirstName());
        employeeOutputDto.setPreposition(employee.getPreposition());
        employeeOutputDto.setLastName(employee.getLastName());
        employeeOutputDto.setShortName(employee.getShortName());
        employeeOutputDto.setDob(employee.getDob());
        employeeOutputDto.setPhoneNumber(employee.getPhoneNumber());
        employeeOutputDto.setEmail(employee.getUser().getEmail());
        employeeOutputDto.setIsEmployed(employee.getIsEmployed());
        employeeOutputDto.setTeam(employee.getTeam());
        employeeOutputDto.setShifts(employee.getShifts());
        employeeOutputDto.setUsername(employee.getUser().getUsername());
        employeeOutputDto.setAuthorities(employee.getUser().getAuthorities());

        return employeeOutputDto;
    }

//    Overload transfer for postmapping

    private Employee transferEmployeeInputDtoToEmployee(EmployeeInputDto employeeInputDto, Team team) {
        Employee employee = new Employee();
        transferEmployeeInputDtoToEmployee(employeeInputDto, employee, team);
        return employee;
    }


    // Overload transfer for putmapping
    private Employee transferEmployeeInputDtoToEmployee(EmployeeInputDto employeeInputDto, Employee employee, Team team) {
        User user = userRepository.findById(employeeInputDto.getUsername()).orElseThrow(() -> new RecordNotFoundException("No user found with username: " + employeeInputDto.getUsername()));
        employee.setFirstName(employeeInputDto.getFirstName());
        employee.setPreposition(employeeInputDto.getPreposition());
        employee.setLastName(employeeInputDto.getLastName());
        employee.setShortName(employeeInputDto.getShortName());
        employee.setDob(employeeInputDto.getDob());
        employee.setPhoneNumber(employeeInputDto.getPhoneNumber());
        employee.setUser(user);
        employee.setIsEmployed(employeeInputDto.getIsEmployed());
        employee.setTeam(team);
        user.setEmployee(employee);
        return employee;
    }


}
