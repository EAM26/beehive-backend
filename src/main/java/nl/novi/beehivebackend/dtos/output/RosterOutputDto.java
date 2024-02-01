package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Shift;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RosterOutputDto {
    private Long id;
    private String name;
    private String teamName;
    private List<LocalDate> weekDates;
    private List<Shift> shifts;
}