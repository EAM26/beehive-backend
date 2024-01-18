package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.services.AbsenceService;
import nl.novi.beehivebackend.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
