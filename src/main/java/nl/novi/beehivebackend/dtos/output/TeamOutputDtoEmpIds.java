package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class TeamOutputDtoEmpIds {

    private String teamName;
    private Boolean isActive;
    private List<Long> employeeIds;



}
