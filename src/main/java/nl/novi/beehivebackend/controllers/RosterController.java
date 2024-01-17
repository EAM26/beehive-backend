//package nl.novi.beehivebackend.controllers;
//
//import jakarta.validation.Valid;
//import nl.novi.beehivebackend.dtos.input.EmployeeInputDto;
//import nl.novi.beehivebackend.dtos.input.RosterInputDto;
//import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
//import nl.novi.beehivebackend.services.RosterService;
//import nl.novi.beehivebackend.utils.ValidationUtil;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//
//@RestController
//@RequestMapping("/rosters")
//public class RosterController {
//
//    private final RosterService rosterService;
//    private final ValidationUtil validationUtil;
//
//    public RosterController(RosterService rosterService, ValidationUtil validationUtil) {
//        this.rosterService = rosterService;
//        this.validationUtil = validationUtil;
//    }
//
//
//    @GetMapping
//    public ResponseEntity<Iterable<RosterOutputDto>> getAllRosters() {
//        return new ResponseEntity<>(rosterService.getAllRosters(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<RosterOutputDto> getRoster(@PathVariable String id) {
//        return new ResponseEntity<>(rosterService.getRoster(id), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Object> createRoster(@Valid @RequestBody RosterInputDto rosterInputDto, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
//        }
//        RosterOutputDto rosterOutputDto = rosterService.createRoster(rosterInputDto);
//            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + rosterOutputDto.id).toUriString());
//            return ResponseEntity.created(uri).body(rosterOutputDto);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateRoster(@PathVariable Long id, @Valid @RequestBody RosterInputDto rosterInputDto, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
//        }
//        return new ResponseEntity<>(rosterService.updateRoster(id, rosterInputDto), HttpStatus.ACCEPTED);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteRoster(@PathVariable Long id) {
//        rosterService.deleteRoster(id);
//        return ResponseEntity.noContent().build();
//    }






//}
