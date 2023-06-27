package nl.novi.beehivebackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

}
