package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AbsenceOutputDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long employeeId;
    private String employeeShortName;


}
