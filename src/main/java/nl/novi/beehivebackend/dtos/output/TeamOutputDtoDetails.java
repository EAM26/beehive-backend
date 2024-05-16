package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamOutputDtoDetails {
    private String teamName;
    private Boolean isActive;
    private List <String> employeesData;
    private List<String> rosterData;

    public TeamOutputDtoDetails(String teamName, boolean isActive, List<String> employeesData, List<String> rosterData) {
        this.teamName = teamName;
        this.isActive = isActive;
        this.employeesData = employeesData;
        this.rosterData = rosterData;
    }

    public TeamOutputDtoDetails() {

    }

    private List<RosterOutputDto> rostersOutputDtos;


}

