package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.exceptions.IsNotEmptyException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;

    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository) {
        this.modelMapper = new ModelMapper();
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
    }


    public Iterable<EmployeeOutputDto> getAllEmployees() {
        List<EmployeeOutputDto> employeeOutputDtos = new ArrayList<>();
        for (Employee employee: employeeRepository.findAll()) {
            employeeOutputDtos.add(convertEmployeeToOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public EmployeeOutputDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        return convertEmployeeToOutputDto(employee);
    }


    public EmployeeOutputDto createEmployee(EmployeeInputDto employeeInputDto) {
        if(employeeInputDto.getTeam() != null) {
            // Check if team exists
            teamRepository.findById(employeeInputDto.getTeam().getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name: " + employeeInputDto.getTeam().getTeamName()));
        }
            Employee employee = employeeRepository.save(convertDtoToEmployee(employeeInputDto));
            return convertEmployeeToOutputDto(employee);
    }

    public EmployeeOutputDto updateEmployee(Long id, EmployeeInputDto employeeInputDto) {
        employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        if(employeeInputDto.getTeam() != null) {
            teamRepository.findById(employeeInputDto.getTeam().getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name: " + employeeInputDto.getTeam().getTeamName()));
        }
        Employee employee = convertDtoToEmployee(employeeInputDto);
        employee.setId(id);
        employeeRepository.save(employee);
        return convertEmployeeToOutputDto(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id " + id));
        if(!employee.getShifts().isEmpty()) {
            throw new IsNotEmptyException("Employee is not empty. First remove all shifts");
        }
        employeeRepository.deleteById(id);
    }



//    Conversion modelmapper methods
    private EmployeeOutputDto convertEmployeeToOutputDto(Employee employee) {
        return modelMapper.map(employee, EmployeeOutputDto.class);
    }


    private Employee convertDtoToEmployee(EmployeeInputDto employeeInputDto) {
        return modelMapper.map(employeeInputDto, Employee.class);
    }

    // TODO: 7-7-2023 Check of deze methode handig is 
    private Boolean checkTeamExist(Team teamName) {
        if(teamRepository.findById(teamName.getTeamName()).isEmpty()) {
            return false;
        }
        return true;
    }
}
