package nl.novi.beehivebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class Team {

    @Id
    private String teamName;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Employee> employees;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Shift> shifts;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Roster> rosters;

    public Team(String teamName, Boolean isActive, List<Employee> employees, List<Shift> shifts, List<Roster> rosters) {
        this.teamName = teamName;
        this.isActive = isActive;
        this.employees = employees;
        this.shifts = shifts;
        this.rosters = rosters;
    }

    public Team() {
    }

    public Team(String teamName, boolean isActive) {
    }
}



