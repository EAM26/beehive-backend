package nl.novi.beehivebackend.dtos.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TeamInputDto {

    @NotBlank
    public String teamName;

}
