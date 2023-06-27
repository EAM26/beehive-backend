package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
