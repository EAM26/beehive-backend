package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RosterRepository extends JpaRepository<Roster, String> {

}
