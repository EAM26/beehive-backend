package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamOutputDtoDetails {
    private String teamName;
    private Boolean isActive;
    private List <String> employeeNames;

    public TeamOutputDtoDetails(String teamName, boolean isActive, List<String> employeeNames) {
        this.teamName = teamName;
        this.isActive = isActive;
        this.employeeNames = employeeNames;
    }

    public TeamOutputDtoDetails() {

    }
//    private List<EmployeeOutputDto> employeesOutputDtos;
//    private List<RosterOutputDto> rostersOutputDtos;


}

