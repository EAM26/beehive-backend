package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class ShiftInputDto {

    @NotNull(message = "Start of shift is required.")
    public LocalDateTime startShift;

    @NotNull(message = "Tnd of shift is required.")
    public LocalDateTime endShift;

    @NotNull(message = "Team name is required.")
    public String teamName;

    public Long employeeId;
}
