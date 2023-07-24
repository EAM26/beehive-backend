package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Team;

import java.time.LocalDate;


@Getter
@Setter
public class RosterOutputDto {

    public Long id;
    public int year;
    public int weekNumber;

    public LocalDate startOfWeek;
    public LocalDate endOfWeek;

    public Team team;
}
