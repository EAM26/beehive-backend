package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Absence;
import nl.novi.beehivebackend.models.Authority;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SelfOutputDto {

    private String username;
    private String email;
    private Set<Authority> authorities;

    private Long employeeId;
    private String firstName;
    private String preposition;
    private String lastName;
    private String shortName;
    private LocalDate dob;
    private String phoneNumber;
    private Boolean isActive;
    private Team team;
    private List<Shift> shifts;
    private List<Absence> absences;

}
