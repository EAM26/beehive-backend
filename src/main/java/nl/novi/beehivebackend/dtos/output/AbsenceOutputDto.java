package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class AbsenceOutputDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long employeeId;
    private String employeeShortName;


}
