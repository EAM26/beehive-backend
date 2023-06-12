package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShiftOutputDto {

    public Long id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
}
