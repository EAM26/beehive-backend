package nl.novi.beehivebackend.dtos.input;

import lombok.Getter;
import lombok.Setter;
import nl.novi.beehivebackend.models.Employee;

import java.util.List;

@Getter
@Setter
public class TeamInputDto {

    public String name;

    public List<Employee> employees;
}
