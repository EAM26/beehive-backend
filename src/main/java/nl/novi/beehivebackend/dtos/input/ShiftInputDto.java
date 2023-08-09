package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftInputDto {

    @NotNull
    public LocalDate startDate;

    @NotNull
    public LocalDate endDate;

    @NotNull
    public LocalTime startTime;

    @NotNull
    public LocalTime endTime;

    @NotNull
    public String rosterId;

    public Long employeeId;
}
