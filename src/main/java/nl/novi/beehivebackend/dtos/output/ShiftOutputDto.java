package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShiftOutputDto {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
