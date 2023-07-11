package nl.novi.beehivebackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

@Getter
@Setter

@Entity
@Table(name="rosters")
public class Roster {

    @Id
    private String id;

    private int year;
    private int weekNumber;

    private LocalDate startOfWeek;
    private LocalDate endOfWeek;



}
