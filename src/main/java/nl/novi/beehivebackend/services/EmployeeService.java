package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.modelMapper = new ModelMapper();
        this.employeeRepository = employeeRepository;
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
            Employee employee = employeeRepository.save(convertDtoToEmployee(employeeInputDto));
            return convertEmployeeToOutputDto(employee);
    }

    public EmployeeOutputDto updateEmployee(Long id, EmployeeInputDto employeeInputDto) {
        employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        Employee employee = convertDtoToEmployee(employeeInputDto);
        employee.setId(id);
        employeeRepository.save(employee);
        return convertEmployeeToOutputDto(employee);
    }

    public void deleteEmployee(Long id) {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("No employee found with id: " + id);
        }
    }





//    Conversion modelmapper methods
    private EmployeeOutputDto convertEmployeeToOutputDto(Employee employee) {
        return modelMapper.map(employee, EmployeeOutputDto.class);
    }


    private Employee convertDtoToEmployee(EmployeeInputDto employeeInputDto) {
        return modelMapper.map(employeeInputDto, Employee.class);
    }


}
