package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


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

    @OneToMany(mappedBy = "roster")
    @JsonIgnore
    private List<Shift> shifts;


}
