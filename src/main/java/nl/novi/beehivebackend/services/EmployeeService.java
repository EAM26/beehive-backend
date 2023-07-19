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
            employeeOutputDtos.add(transferEmployeeToEmployeeOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public EmployeeOutputDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        return transferEmployeeToEmployeeOutputDto(employee);
    }


    public EmployeeOutputDto createEmployee(EmployeeInputDto employeeInputDto) {
        if(employeeInputDto.getTeam() != null) {
            // Check if team exists
            teamRepository.findById(employeeInputDto.getTeam().getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name: " + employeeInputDto.getTeam().getTeamName()));
        }
            Employee employee = employeeRepository.save(transferEmployeeInputDtoToEmployee(employeeInputDto));
            return transferEmployeeToEmployeeOutputDto(employee);
    }

    public EmployeeOutputDto updateEmployee(Long id, EmployeeInputDto employeeInputDto) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        if(employeeInputDto.getTeam() != null) {
            teamRepository.findById(employeeInputDto.getTeam().getTeamName()).orElseThrow(() -> new RecordNotFoundException("No team found with name: " + employeeInputDto.getTeam().getTeamName()));
        }

//        modelMapper.map(employeeInputDto, employee);
        employee = transferEmployeeInputDtoToEmployee(employeeInputDto, employee);
        employeeRepository.save(employee);
        return transferEmployeeToEmployeeOutputDto(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id " + id));
        if(!employee.getShifts().isEmpty()) {
            throw new IsNotEmptyException("Employee is not empty. First remove all shifts");
        }
        employeeRepository.deleteById(id);
    }



//    Conversion modelmapper methods
//    private EmployeeOutputDto convertEmployeeToOutputDto(Employee employee) {
//        return modelMapper.map(employee, EmployeeOutputDto.class);
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
        employeeOutputDto.setEmail(employee.getEmail());
        employeeOutputDto.setPassword(employee.getPassword());
        employeeOutputDto.setIsEmployed(employee.getIsEmployed());
        employeeOutputDto.setTeam(employee.getTeam());
        employeeOutputDto.setShifts(employee.getShifts());

        return employeeOutputDto;
    }

    private Employee transferEmployeeInputDtoToEmployee(EmployeeInputDto employeeInputDto) {
        Employee employee = new Employee();
        transferEmployeeInputDtoToEmployee(employeeInputDto, employee);
        return employee;
    }

    private Employee transferEmployeeInputDtoToEmployee(EmployeeInputDto employeeInputDto, Employee employee) {
        employee.setFirstName(employeeInputDto.getFirstName());
        employee.setPreposition(employeeInputDto.getPreposition());
        employee.setLastName(employeeInputDto.getLastName());
        employee.setShortName(employeeInputDto.getShortName());
        employee.setDob(employeeInputDto.getDob());
        employee.setPhoneNumber(employeeInputDto.getPhoneNumber());
        employee.setEmail(employeeInputDto.getEmail());
        employee.setPassword(employeeInputDto.getPassword());
        employee.setIsEmployed(employeeInputDto.getIsEmployed());
        employee.setTeam(employeeInputDto.getTeam());

        return employee;
    }


//    private Employee convertDtoToEmployee(EmployeeInputDto employeeInputDto) {
//        return modelMapper.map(employeeInputDto, Employee.class);
//    }

    // TODO: 7-7-2023 Check of deze methode handig is 
    private Boolean checkTeamExist(Team teamName) {
        if(teamRepository.findById(teamName.getTeamName()).isEmpty()) {
            return false;
        }
        return true;
    }
}
