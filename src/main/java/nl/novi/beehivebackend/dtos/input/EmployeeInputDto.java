package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


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
    @Size(max = 20, message = "Short name must be less than or equal to 20 characters")
    public String shortName;


//    @Past(message = "Dob should be in the past.")
    public LocalDate dob;


    public String phoneNumber;


    @NotNull(message="username field is required.")
    public String username;


    @NotNull(message="Employed field is required.")
    public Boolean isActive;

    @NotNull(message="Team field is required.")
    public String teamName;

}
