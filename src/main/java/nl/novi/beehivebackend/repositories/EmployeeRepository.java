package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.dtos.output.EmployeeOutputDto;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.Team;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Boolean existsByShortNameIgnoreCase(String shortName);
    List<Employee> findAllByTeam(Team team);
    List<Employee> findAllByTeam(Team team, Sort sort);


}
