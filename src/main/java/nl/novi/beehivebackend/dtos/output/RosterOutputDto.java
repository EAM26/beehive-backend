package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Shift;

import java.util.List;

@Getter
@Setter
public class RosterOutputDto {
    private String name;
    private List<Shift> shifts;
}