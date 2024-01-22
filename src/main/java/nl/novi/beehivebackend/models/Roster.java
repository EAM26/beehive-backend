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

    public Roster(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    @Id
    private String name;

    @OneToMany(mappedBy = "roster", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Shift> shifts;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    public Roster() {

    }
}
