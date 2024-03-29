package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamInputDto {


    @NotBlank(message = "Team name is required.")
    private String teamName;

    @NotNull(message = "Team status is required")
    private Boolean isActive;

    public TeamInputDto(String teamName, Boolean isActive) {
        this.teamName = teamName;
        this.isActive = isActive;
    }

    public TeamInputDto() {
    }
}
