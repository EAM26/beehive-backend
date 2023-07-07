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
    private String firstName;
    private String preposition;
    private String lastName;
    private LocalDate dob;
    private String phoneNumber;
    private String email;
    private String password;
    private Boolean isEmployed;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team")
    private Team team;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Shift> shifts;
}
