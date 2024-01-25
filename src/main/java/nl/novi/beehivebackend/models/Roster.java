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

    //    public Roster(String name, Team team) {
//        this.name = name;
//        this.team = team;
//    }
    public Roster() {

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


    public String createRosterName() {
        return (week + "-" + year + "-" + team.getTeamName());
    }
}
