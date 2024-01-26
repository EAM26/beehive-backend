package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startShift;

    @Column(nullable = false)
    private LocalDateTime endShift;

    @Column(nullable = false)
    private int weekNumber;

    @Column(nullable = false)
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "roster_id")
    private Roster roster;


    public Shift(Long id, LocalDateTime startShift, LocalDateTime endShift, int weekNumber, int year, Team team, Employee employee, Roster roster) {
        this.id = id;
        this.startShift = startShift;
        this.endShift = endShift;
        this.weekNumber = weekNumber;
        this.year = year;
        this.team = team;
        this.employee = employee;
        this.roster = roster;
    }

    public Shift() {
    }
}
