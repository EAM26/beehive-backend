package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftOutputDto {

    public Long id;

    public LocalDate startDate;
    public LocalDate endDate;

    public LocalTime startTime;
    public LocalTime endTime;

    public Employee employee;
    public Roster roster;
}
