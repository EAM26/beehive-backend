package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserInputDto {

    @NotBlank(message = "User name is required.")
    @Size(min = 2, max = 20, message = "User name between 2 and 20 characters")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Valid email is required.")
    private String email;


    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()\\[{}\\]:;',?/*~$^+=<>]).{8,20}$", message =
            "1. Password must contain at least one digit [0-9].\n" +
                    "2. Password must contain at least one lowercase Latin character [a-z].\n" +
                    "3. Password must contain at least one uppercase Latin character [A-Z].\n" +
                    "4. Password must contain at least one special character.\n" +
                    "5. Password must contain a length of at least 8 characters and a maximum of 20 characters.")
    private String password;

    @NotNull(message = "User status is required")
    private Boolean isDeleted;


    @NotBlank(message = "Role is required.")
    private String userRole;

}
