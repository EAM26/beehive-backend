package nl.novi.beehivebackend.controllers;

import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.services.EmployeeService;
import nl.novi.beehivebackend.utils.ValidationUtil;
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
    private final ValidationUtil validationUtil;


    public EmployeeController(EmployeeService employeeService, ValidationUtil validationUtil) {
        this.employeeService = employeeService;
        this.validationUtil = validationUtil;
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
            return ResponseEntity.badRequest().body(this.validationUtil.validationMessage(bindingResult).toString());
        }
        EmployeeOutputDto employeeOutputDto = employeeService.createEmployee(employeeInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + employeeOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(employeeOutputDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeInputDto employeeInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(this.validationUtil.validationMessage(bindingResult).toString());
        }
//        this.employeeService.updateEmployee(id, employeeInputDto);
    }



}
