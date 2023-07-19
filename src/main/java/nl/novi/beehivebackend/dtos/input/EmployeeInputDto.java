package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Team;


import java.time.LocalDate;

@Getter
@Setter
public class EmployeeInputDto {

    @NotBlank(message = "First name is required.")
    public String firstName;
    public String preposition;

    @NotBlank(message = "Last name is required.")
    public String lastName;


    @NotBlank(message = "Short name is required")
    public String shortName;

    @Past(message = "Dob should be in the past.")
    public LocalDate dob;
    public String phoneNumber;

    @NotBlank(message = "Email is required.")
    @Email(message = "Valid email is required.")
    public String email;

    @NotBlank (message = "Password is required.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = "1. Password must contain at least one digit [0-9].\n" +
            "2. Password must contain at least one lowercase Latin character [a-z].\n" +
            "3. Password must contain at least one uppercase Latin character [A-Z].\n" +
            "4. Password must contain at least one special character like ! @ # & ( ).\n" +
            "5. Password must contain a length of at least 8 characters and a maximum of 20 characters.")
    public String password;

    @NotNull(message="Employed field is required.")
    public Boolean isEmployed;


    public Team team;

}
