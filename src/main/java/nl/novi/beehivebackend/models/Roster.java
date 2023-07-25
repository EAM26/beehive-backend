package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;



@Getter
@Setter

@Entity
@Table(name = "rosters")
public class Roster {

    @Id
    private String id;

    private int year;
    private int weekNumber;


    private LocalDate startOfWeek;
    private LocalDate endOfWeek;

    @OneToMany(mappedBy = "roster")
    @JsonIgnore
    private List<Shift> shifts;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team")
    private Team team;

//


//    public void setYear(int year) {
//        this.year = year;
//    }
//
//    public void setWeekNumber(int weekNumber) {
//        this.weekNumber = weekNumber;
//    }


//    private void calculateDates() {
//
//        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
//
//        // Define the week-based year and week fields
//        TemporalField weekBasedYearField = WeekFields.ISO.weekBasedYear();
//        TemporalField weekOfWeekBasedYearField = WeekFields.ISO.weekOfWeekBasedYear();
//
//        // Calculate the start and end dates of the week
//        this.startOfWeek = firstDayOfYear.with(weekBasedYearField, year)
//                .with(weekOfWeekBasedYearField, weekNumber)
//                .with(java.time.DayOfWeek.MONDAY);
//        this.endOfWeek = startOfWeek.plusDays(6);
//    }

}
