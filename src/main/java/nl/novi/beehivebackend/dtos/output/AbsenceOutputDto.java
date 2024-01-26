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


    public AbsenceOutputDto(Long id, LocalDate startDate, LocalDate endDate, Long employeeId, String employeeShortName) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employeeId = employeeId;
        this.employeeShortName = employeeShortName;
    }

    public AbsenceOutputDto() {
    }
}
