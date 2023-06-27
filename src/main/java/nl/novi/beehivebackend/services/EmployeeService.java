package nl.novi.beehivebackend.services;


import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.exceptions.IllegalValueException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
            employeeOutputDtos.add(convertEmployeeToDto(employee));
        }
        return employeeOutputDtos;
    }

    public EmployeeOutputDto getEmployee(Long id) {
        Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
        return this.convertEmployeeToDto(employee);
    }


    public EmployeeOutputDto createEmployee(EmployeeInputDto employeeInputDto) {
            Employee employee = this.employeeRepository.save(this.convertDtoToEmployee(employeeInputDto));
            return this.convertEmployeeToDto(employee);
    }

    //    To do: Add validation double BSN check

//    private Boolean checkEmployeeDto(EmployeeInputDto employeeInputDto) {
//        if (this.employeeRepository.findBySocSecNum(employeeInputDto.socSecNum)) {
//            throw new IllegalValueException("Social security number already exists");
//        } else if(employeeInputDto.socSecNum.length() < 8 || employeeInputDto.socSecNum.length() > 9) {
//            throw new IllegalValueException("Social security number has illegal length");
//        }
//        return true;
//    }

//    Conversion modelmapper methods
    private EmployeeOutputDto convertEmployeeToDto(Employee employee) {
        return modelMapper.map(employee, EmployeeOutputDto.class);
    }

    private Employee convertDtoToEmployee(EmployeeInputDto employeeInputDto) {
        return modelMapper.map(employeeInputDto, Employee.class);
    }
}
