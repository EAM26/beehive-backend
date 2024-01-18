package nl.novi.beehivebackend.controllers;

import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.AbsenceInputDto;
import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.services.AbsenceService;
import nl.novi.beehivebackend.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("absences")
public class AbsenceController {

    private final AbsenceService absenceService;
    private final ValidationUtil validationUtil;
    public AbsenceController(AbsenceService absenceService, ValidationUtil validationUtil) {
        this.absenceService = absenceService;
        this.validationUtil = validationUtil;

    }

    @GetMapping
    public ResponseEntity<Iterable<AbsenceOutputDto>> getAllAbsences() {
        return new ResponseEntity<>(absenceService.getAllAbsences(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbsenceOutputDto> getSingleAbsence(@PathVariable Long id) {
        return new ResponseEntity<>(absenceService.getSingleAbsence( id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createAbsence(@Valid @RequestBody AbsenceInputDto absenceInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        AbsenceOutputDto absenceOutputDto = absenceService.createAbsence(absenceInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + absenceOutputDto.getId()).toUriString());
        return ResponseEntity.created(uri).body(absenceOutputDto);
    }
}
