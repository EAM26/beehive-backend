package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Team;


@Getter
@Setter
public class RosterInputDto {
    @NotNull
    @Min(value = 2022, message = "Year only from 2022-2040")
    @Max(value = 2040, message = "Yer only from 2022-2040")
    public int year;

    @NotNull
    @Min(value = 1, message = "Week only from 0-52")
    @Max(value = 52, message = "Week only from 0-52")
    public int weekNumber;

    @NotNull(message="Team field is required.")
    public Long teamId;

}
