package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import nl.novi.beehivebackend.models.Authority;

import java.util.Set;

public class UserInputDto {

    public String username;

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message =
            "1. Password must contain at least one digit [0-9].\n" +
            "2. Password must contain at least one lowercase Latin character [a-z].\n" +
            "3. Password must contain at least one uppercase Latin character [A-Z].\n" +
            "4. Password must contain at least one special character like ! @ # & ( ).\n" +
            "5. Password must contain a length of at least 8 characters and a maximum of 20 characters.")
    public String password;

    @NotBlank(message = "Email is required.")
    @Email(message = "Valid email is required.")
    public String email;

    public Long employeeId;
    public Set<Authority> authorities;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
