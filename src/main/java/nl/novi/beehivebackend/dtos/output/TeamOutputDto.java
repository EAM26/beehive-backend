package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class TeamOutputDto {
    public TeamOutputDto() {
    }

    private String teamName;
    private Boolean isActive;


    public TeamOutputDto(String teamName, Boolean isActive) {
        this.teamName = teamName;
        this.isActive = isActive;
    }
}
