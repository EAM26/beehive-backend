package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RosterInputDto {

    @Min(value = 2023, message = "Value must be greater than or equal to 1")
    @Max(value = 2040, message = "Value must be less than or equal to 100")
    public int year;
    @Min(value = 0, message = "No negative week values allowed")
    @Max(value = 52, message = "Week number must be less than or equal to 52")
    public int weekNumber;

}
