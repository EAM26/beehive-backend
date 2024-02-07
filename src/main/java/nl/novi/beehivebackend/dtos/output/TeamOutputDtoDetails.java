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
    private List<EmployeeOutputDto> employeesOutputDtos;
    private List<RosterOutputDto> rostersOutputDtos;

    public TeamOutputDtoDetails() {
    }

    public TeamOutputDtoDetails(String teamName, Boolean isActive, List<EmployeeOutputDto> employeesOutputDtos, List<RosterOutputDto> rostersOutputDtos) {
        this.teamName = teamName;
        this.isActive = isActive;
        this.employeesOutputDtos = employeesOutputDtos;
        this.rostersOutputDtos = rostersOutputDtos;
    }
}

