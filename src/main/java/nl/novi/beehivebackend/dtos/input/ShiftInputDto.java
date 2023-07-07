package nl.novi.beehivebackend.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftInputDto {


    public LocalDate startDate;
    public LocalDate endDate;

    public LocalTime startTime;
    public LocalTime endTime;
}
