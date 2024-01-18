package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e.id FROM Employee e")
    List<Long> findAllIds();
    Boolean existsByShortNameIgnoreCase(String shortName);


}
