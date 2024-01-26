package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AbsenceInputDto {
    @NotNull(message = "Start Date is required")
    private LocalDate startDate;

    @NotNull(message = "End Date is required")
    private LocalDate endDate;

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    public AbsenceInputDto(LocalDate startDate, LocalDate endDate, Long employeeId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.employeeId = employeeId;
    }


    @AssertTrue(message = "End Date must be after or equal to Start Date")
    public boolean isEndDateValid() {
        return !endDate.isBefore(startDate);
    }


}
