package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;


import java.util.List;

@Getter
@Setter
public class TeamOutputDto {

    public Long id;
    public String teamName;
    public List<Employee> employees;
    public List<Roster> rosters;

}
