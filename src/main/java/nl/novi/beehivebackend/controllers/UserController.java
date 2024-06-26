package nl.novi.beehivebackend.controllers;


import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDtoDetails;
import nl.novi.beehivebackend.services.UserService;
import nl.novi.beehivebackend.utils.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final ValidationUtil validationUtil;

    public UserController(UserService userService, ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
        this.userService = userService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<UserOutputDto>> getUsers(@RequestParam(required = false)Boolean hasEmployee, @RequestParam(required = false) Boolean isDeleted) {

        return ResponseEntity.ok().body(userService.getUsers(hasEmployee, isDeleted));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserOutputDtoDetails> getSingleUser(@PathVariable("username") String username) {
        UserOutputDtoDetails optionalUser = userService.getSingleUser(username);
        return ResponseEntity.ok().body(optionalUser);
    }

    @GetMapping(value = "/self")
    public ResponseEntity<UserOutputDtoDetails> getSelfAsUser() {
        UserOutputDtoDetails selfAsUser = userService.getSelfAsUser();
        return ResponseEntity.ok().body(selfAsUser);
    }


    @PostMapping(value = "")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        UserOutputDto userOutputDto = userService.createUser(userInputDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(userOutputDto.getUsername()).toUri();
        return ResponseEntity.created(location).body(userOutputDto.getUsername());
    }


    @PutMapping(value = "/{username}")
    public ResponseEntity<String> updateUser(@PathVariable("username") String username, @Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        UserOutputDto userOutputDto = userService.updateUser(username, userInputDto);
        return new ResponseEntity<>(userOutputDto.getUsername() + " updated.", HttpStatus.OK);
    }

    @PutMapping(value = "/self")
    public ResponseEntity<String> updateSelf(@Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        UserOutputDto userOutputDto = userService.updateSelf(userInputDto);
        return new ResponseEntity<>(userOutputDto.getUsername() + " updated.", HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String  username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }


}