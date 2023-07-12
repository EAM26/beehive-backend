package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Objects;


@Getter
@Setter

@Entity
@ToString
@Table(name = "rosters")
public class Roster {

    @Id
    private String id;

    private int year;
    private int weekNumber;

    @Setter(AccessLevel.NONE)
    private LocalDate startOfWeek;
    @Setter(AccessLevel.NONE)
    private LocalDate endOfWeek;

    @OneToMany(mappedBy = "roster")
    @JsonIgnore
    private List<Shift> shifts;

    public void setYear(int year) {
        this.year = year;
        resetCalculations();
        recalculateDates();
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        resetCalculations();
        recalculateDates();
    }

    private void resetCalculations() {
        this.startOfWeek = null;
        this.endOfWeek = null;
    }
    private void recalculateDates() {
        if (year == 0 || weekNumber == 0) return;

        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);

        // Define the week-based year and week fields
        TemporalField weekBasedYearField = WeekFields.ISO.weekBasedYear();
        TemporalField weekOfWeekBasedYearField = WeekFields.ISO.weekOfWeekBasedYear();

        // Calculate the start and end dates of the week
        this.startOfWeek = firstDayOfYear.with(weekBasedYearField, year)
                .with(weekOfWeekBasedYearField, weekNumber)
                .with(java.time.DayOfWeek.MONDAY);
        this.endOfWeek = startOfWeek.plusDays(6);
    }

}
