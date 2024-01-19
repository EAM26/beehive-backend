package nl.novi.beehivebackend.dtos.output;

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
public class ShiftOutputDto {

    public Long id;

    public LocalDateTime startShift;
    public LocalDateTime  endShift;

    public String  employeeShortName;
    public String teamName;
}
