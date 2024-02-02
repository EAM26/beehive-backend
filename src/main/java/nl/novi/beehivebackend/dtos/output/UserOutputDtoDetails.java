package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserOutputDtoDetails {

    private String username;
    private String email;
    private Set<Authority> authorities;
    private Boolean isDeleted;

    private Employee employee;


    private Team team;
    private List<Shift> shifts;
    private List<Absence> absences;

}
