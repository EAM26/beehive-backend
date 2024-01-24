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
@Table(name="employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Personal details
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String preposition;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String shortName;

    private LocalDate dob;
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team_name")
    private Team team;


    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Shift> shifts;

    @OneToOne(mappedBy = "employee")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Absence> absences;

}
