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
        for (Employee employee: this.employeeRepository.findAll()) {
            employeeOutputDtos.add(convertEmployeeToOutputDto(employee));
        }
        return employeeOutputDtos;
    }

    public EmployeeOutputDto getEmployee(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        return this.convertEmployeeToOutputDto(employee);
    }


    public EmployeeOutputDto createEmployee(EmployeeInputDto employeeInputDto) {
            Employee employee = this.employeeRepository.save(this.convertDtoToEmployee(employeeInputDto));
            return this.convertEmployeeToOutputDto(employee);
    }

    public EmployeeOutputDto updateEmployee(Long id, EmployeeInputDto employeeInputDto) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        employee = this.convertDtoToEmployee(employeeInputDto);
        employee.setId(id);
        this.employeeRepository.save(employee);
        return this.convertEmployeeToOutputDto(employee);
    }



//    Conversion modelmapper methods
    private EmployeeOutputDto convertEmployeeToOutputDto(Employee employee) {
        return modelMapper.map(employee, EmployeeOutputDto.class);
    }

    private Employee convertDtoToEmployee(EmployeeInputDto employeeInputDto) {
        Field[] fields = EmployeeInputDto.class.getFields();
        for (Field field: fields) {
            System.out.println(field);
        }
        return modelMapper.map(employeeInputDto, Employee.class);
    }


}
