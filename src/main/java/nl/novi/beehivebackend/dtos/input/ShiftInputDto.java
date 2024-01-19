package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class ShiftInputDto {

    @NotNull(message = "Start of shift is required.")
    private LocalDateTime startShift;

    @NotNull(message = "End of shift is required.")
    private LocalDateTime endShift;

    @NotNull(message = "Team name is required.")
    private String teamName;

    private Long employeeId;
}
