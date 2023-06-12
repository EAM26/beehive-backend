package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeInputDto {

    @NotBlank(message = " is required.")
    @Size(min=2, max = 20)
    public String firstName;
    @NotBlank(message = " is required.")
    @Size(min=1, max = 10)
    public String initials;
    public String preposition;
    @NotBlank(message = " is required.")
    @Size(min=2, max = 100)
    public String lastName;

    public String address;
    public LocalDate dob;
    public String phoneNumber;

    @NotBlank
    @Email(message = " is required")
    public String email;
    public String socialSecurityNumber;


    public LocalDate hireDate;
    public Boolean isEmployed;
}
