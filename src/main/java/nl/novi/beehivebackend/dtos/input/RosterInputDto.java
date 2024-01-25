package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RosterInputDto {

    @NotNull(message = "Week is required")
    private int week;

    @NotNull(message = "Year is required")
    private int year;

    @NotNull(message = "Team name is required")
    private String teamName;
}
