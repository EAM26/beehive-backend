package nl.novi.beehivebackend.dtos.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeInputDto {

    @NotBlank(message = "First name is required.")
    public String firstName;
    public String preposition;

    @NotBlank(message = "Last name is required.")
    public String lastName;

    @Past(message = "Dob should be in the past.")
    public LocalDate dob;
    public String phoneNumber;

    @NotBlank(message = "Email is required.")
    @Email(message = "Valid email is required.")
    public String email;

    @NotNull(message="Employed is required.")
    public Boolean isEmployed;
}
