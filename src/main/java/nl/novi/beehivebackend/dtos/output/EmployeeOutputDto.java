package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeOutputDto {

    public Long id;
    public String firstName;
    public String preposition;
    public String lastName;
    public LocalDate dob;
    public String phoneNumber;
    public String email;
    public String password;
    public Boolean isEmployed;
    public Team team;
    public List<Shift> shifts;
}
