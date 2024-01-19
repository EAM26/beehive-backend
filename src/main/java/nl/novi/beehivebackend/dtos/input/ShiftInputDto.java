package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftInputDto {

    @NotNull
    public LocalDateTime startShift;

    @NotNull
    public LocalDateTime endShift;

    @NotNull
    public String teamName;

    public Long employeeId;
}
