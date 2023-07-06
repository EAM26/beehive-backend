package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ShiftOutputDto {

    public Long id;

    public LocalDate startDate;
    public LocalDate endDate;

    public LocalTime startTime;
    public LocalTime endTime;
}
