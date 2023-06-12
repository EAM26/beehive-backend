package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.services.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<Iterable<ShiftOutputDto>> getAllShifts() {
        return new ResponseEntity<>(this.shiftService.getAllShifts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftOutputDto> getShift(@PathVariable Long id) {
        return new ResponseEntity<>(this.shiftService.getShift(id), HttpStatus.OK);
    }
}
