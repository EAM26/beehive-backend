package nl.novi.beehivebackend.controllers;

import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.services.ShiftService;
import org.springframework.http.HttpStatus;
import nl.novi.beehivebackend.utils.ValidationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;
    private final ValidationUtil validationUtil;

    public ShiftController(ShiftService shiftService, ValidationUtil validationUtil) {
        this.shiftService = shiftService;
        this.validationUtil =validationUtil;
    }

    @GetMapping
    public ResponseEntity<Iterable<ShiftOutputDto>> getAllShifts() {
        return new ResponseEntity<>(shiftService.getAllShifts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftOutputDto> getShift(@PathVariable Long id) {
        return new ResponseEntity<>(shiftService.getShift(id), HttpStatus.OK);
    }

    @GetMapping("/roster/{id}")
    public ResponseEntity<Iterable<ShiftOutputDto>> getAllShiftsByRoster(@PathVariable Long id) {
        return new ResponseEntity<>(shiftService.getAllShiftsByRoster(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createShift(@Valid @RequestBody ShiftInputDto shiftInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }

        ShiftOutputDto shiftOutputDto = shiftService.createShift(shiftInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + shiftOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(shiftOutputDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateShift(@PathVariable Long id, @Valid @RequestBody ShiftInputDto shiftInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }

        return new ResponseEntity<>(shiftService.updateShift(id, shiftInputDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
}
