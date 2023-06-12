package nl.novi.beehivebackend.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShiftInputDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
