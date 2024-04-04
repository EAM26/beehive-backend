package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.ImageData;
import nl.novi.beehivebackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {

    void deleteByEmployeeId(Long id);
    Optional<ImageData> findByEmployee(Employee employee);

}
