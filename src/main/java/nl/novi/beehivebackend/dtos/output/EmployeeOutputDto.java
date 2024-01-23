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
public class EmployeeOutputDto {

    public Long id;
    public String firstName;
    public String preposition;
    public String lastName;
    public String shortName;
    public LocalDate dob;
    public String phoneNumber;
    public String email;
    public Boolean isEmployed;
    public Team team;
    public List<Shift> shifts;
    public List<Absence> absences;
    public String username;
    public Set<Authority> authorities;
}
