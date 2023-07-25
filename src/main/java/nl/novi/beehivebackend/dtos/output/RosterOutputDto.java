package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class RosterOutputDto {

    public String id;
    public int year;
    public int weekNumber;

    public LocalDate startOfWeek;
    public LocalDate endOfWeek;

    public List<Shift> shifts;
    public Team team;
}
