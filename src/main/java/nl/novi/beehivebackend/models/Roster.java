package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rosters")
public class Roster {

    public Roster(Long id, int week, int year, List<Shift> shifts, Team team) {
        this.id = id;
        this.week = week;
        this.year = year;
        this.shifts = shifts;
        this.team = team;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int week;
    private int year;

    @OneToMany(mappedBy = "roster", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Shift> shifts;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    public Roster() {
    }

    public Roster(int weekNumber, int year, Team team) {
        this.week = weekNumber;
        this.year = year;
        this.team = team;
    }

    public Roster(int weekNumber, int year, String teamName) {
    }


    public String createRosterName() {
        return (week + "-" + year + "-" + team.getTeamName());
    }
}
