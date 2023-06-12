package nl.novi.beehivebackend.controllers;

import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.services.EmployeeService;
import nl.novi.beehivebackend.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    private ValidationUtils validationUtils;


    public EmployeeController(EmployeeService employeeService, ValidationUtils validationUtils) {
        this.employeeService = employeeService;
        this.validationUtils = validationUtils;
    }

    @GetMapping
    public ResponseEntity<Iterable<EmployeeOutputDto>> getAllEmployees() {
        return new ResponseEntity<>(this.employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeOutputDto> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(this.employeeService.getEmployee(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeInputDto employeeInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(this.validationUtils.validationMessage(bindingResult).toString());
        }
        EmployeeOutputDto employeeOutputDto = employeeService.createEmployee(employeeInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + employeeOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(employeeOutputDto);

    }
}
