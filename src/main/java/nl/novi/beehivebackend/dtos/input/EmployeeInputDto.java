package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
public class EmployeeInputDto {

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 20, message = "First name between 2 and 20 characters")
    public String firstName;

    @Size(max = 10, message = "Preposition max 10 characters")
    public String preposition;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 20, message = "Last name between 2 and 20 characters")
    public String lastName;


    @NotBlank(message = "Short name is required")
    @Size(min = 4, max = 20, message = "Short name between 4 and 20 characters")
    public String shortName;


    @Past(message = "Dob should be in the past.")
    public LocalDate dob;


    @Pattern(regexp = "^\\d*$", message = "Phone number can only contain numbers")
    public String phoneNumber;


    @NotNull(message="username field is required.")
    public String username;


    @NotNull(message="Employed field is required.")
    public Boolean isActive;

    @NotNull(message="Team field is required.")
    public String teamName;

}
