package nl.novi.beehivebackend.controllers;


import jakarta.validation.Valid;
import nl.novi.beehivebackend.dtos.input.UserRoleInputDto;
import nl.novi.beehivebackend.dtos.input.UserInputDto;
import nl.novi.beehivebackend.dtos.output.UserOutputDto;
import nl.novi.beehivebackend.services.UserService;
import nl.novi.beehivebackend.utils.ValidationUtil;
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

//    @GetMapping(value = "/{username}/authorities")
//    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
//        return ResponseEntity.ok().body(userService.getAuthorities(username));
//    }

//    // TODO: 14-8-2023 Wijzg userRole in String, zodat de Param gestest kan worden en evt Exception kan gooien als het geen UserRole is.
//    @PostMapping(value = "")
//    public ResponseEntity<Object> createNewUser(@Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult, @RequestParam(value = "userrole", required = false) String roleName) {
//        if (bindingResult.hasFieldErrors()) {
//            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
//        }
//        UserOutputDto userOutputDto = userService.createUser(userInputDto, roleName);
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
//                .buildAndExpand(userOutputDto.getUsername()).toUri();
//        return ResponseEntity.created(location).build();
//    }

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
    public ResponseEntity<Object> updateExistingUser(@PathVariable("username") String username, @Valid @RequestBody UserInputDto userInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(validationUtil.validationMessage(bindingResult).toString());
        }
        userService.updateUser(username, userInputDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/addauth/{username}")
    public ResponseEntity<Object> updateUserAuthority(@PathVariable("username") String username, @RequestBody UserRoleInputDto userRole) {
        userService.addAuthority(username, userRole.getRoleName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteExistingUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String role) {
        userService.removeAuthority(username, role);
        return ResponseEntity.noContent().build();
    }


}