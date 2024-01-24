package nl.novi.beehivebackend.controllers;


import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.UserRoleInputDto;
import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
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
    public ResponseEntity<List<UserOutputDto>> getUsers(@RequestParam(required = false) Boolean isDeleted) {
        if(isDeleted == null) {
            return ResponseEntity.ok().body(userService.getUsers());
        }

        return ResponseEntity.ok().body(userService.getUsers(isDeleted));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserOutputDto> getSingleUser(@PathVariable("username") String username) {
        UserOutputDto optionalUser = userService.getSingleUser(username);
        return ResponseEntity.ok().body(optionalUser);
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



//    @PutMapping(value = "/add_auth/{username}")
//    public ResponseEntity<String> addUserAuthority(@PathVariable("username") String username, @RequestBody UserRoleInputDto userRole) {
//        userService.addAuthority(username, userRole.getRoleName());
//        return new ResponseEntity<>(userRole.getRoleName() + " added to authorities", HttpStatus.OK);
//    }
//    @PutMapping(value = "/remove_auth/{username}")
//    public ResponseEntity<String> removeUserAuthority(@PathVariable("username") String username, @RequestBody UserRoleInputDto userRole) {
//        userService.removeAuthority(username, userRole.getRoleName());
//        return new ResponseEntity<>(userRole.getRoleName() + " removed from authorities", HttpStatus.OK);
//    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<String> updateUser(@PathVariable("username") String username, @Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        UserOutputDto userOutputDto = userService.updateUser(username, userInputDto);
        return new ResponseEntity<>(userOutputDto.getUsername() + " updated.", HttpStatus.OK);
    }




}