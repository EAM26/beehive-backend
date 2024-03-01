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
    public Employee(Long id, String firstName, String preposition, String lastName, String shortName, LocalDate dob, String phoneNumber, Boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.preposition = preposition;
        this.lastName = lastName;
        this.shortName = shortName;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Personal details
    @Column(nullable = false)
    private String firstName;

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
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;


    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Shift> shifts;

    @OneToOne
    @JoinColumn(name = "username")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Absence> absences;


    @OneToOne(mappedBy = "employee")
    private ImageData imageData;

    public Employee() {

    }
}
