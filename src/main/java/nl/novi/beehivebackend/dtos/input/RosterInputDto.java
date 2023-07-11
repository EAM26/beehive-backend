package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RosterInputDto {
    @NotNull
    @Min(value = 2023, message = "Year only from 2023-2040")
    @Max(value = 2040, message = "Yer only from 2023-2040")
    public int year;

    @NotNull
    @Min(value = 0, message = "Week only from 0-52")
    @Max(value = 52, message = "Week only from 0-52")
    public int weekNumber;

}