package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

}
