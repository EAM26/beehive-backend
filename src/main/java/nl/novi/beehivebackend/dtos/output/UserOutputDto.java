package nl.novi.beehivebackend.dtos.output;

import nl.novi.beehivebackend.models.Authority;
import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;

import java.util.Set;

@Getter
@Setter
public class UserOutputDto {

    private String username;
    private String email;
    private Boolean isDeleted;

    private Employee employee;

    private Set<Authority> authorities;



}

