package nl.novi.beehivebackend.controllers;

import jakarta.transaction.Transactional;
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
    public ResponseEntity<Iterable<EmployeeOutputDto>> getAllEmployees(@RequestParam(required = false) String teamName) {
        if (teamName != null) {
            return new ResponseEntity<>(employeeService.getAllEmployees(teamName), HttpStatus.OK);
        }
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);

    }


    // TODO: 28-6-2023 Add check to only see self
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeOutputDto> getSingleEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getSingleEmployee(id), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/shift/{id}")
    public ResponseEntity<Iterable<EmployeeOutputDto>> getAvailableEmployees(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getAvailableEmployees(id), HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeInputDto employeeInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        EmployeeOutputDto employeeOutputDto = employeeService.createEmployee(employeeInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + employeeOutputDto.getId()).toUriString());
        return ResponseEntity.created(uri).body(employeeOutputDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeInputDto employeeInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        return new ResponseEntity<>(employeeService.updateEmployee(employeeInputDto, id), HttpStatus.ACCEPTED);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteEmployee(@PathVariable Long id) {
//        employeeService.deleteEmployee(id);
//        return ResponseEntity.noContent().build();
//    }


}
