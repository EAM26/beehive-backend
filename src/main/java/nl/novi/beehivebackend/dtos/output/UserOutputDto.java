package nl.novi.beehivebackend.dtos.output;

import nl.novi.beehivebackend.models.Authority;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserOutputDto {

    private String username;
    private String email;
    private Long employeeId;
    private Boolean isDeleted;

    private Set<Authority> authorities;



}

