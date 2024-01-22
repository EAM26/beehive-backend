package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;

import java.util.List;

@Getter
@Setter
public class TeamOutputDtoDetails {
    private String teamName;
    private Boolean isActive;
    private List<Employee> employees;
    private List<Roster> rosters;
}

