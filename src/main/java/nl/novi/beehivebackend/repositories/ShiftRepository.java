package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByEmployeeId(Long id);

}
