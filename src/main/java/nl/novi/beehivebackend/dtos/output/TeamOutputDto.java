package nl.novi.beehivebackend.dtos.output;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;

import java.util.List;

@Getter
@Setter
public class TeamOutputDto {

    public String teamName;
    public List<Employee> employees;
}
