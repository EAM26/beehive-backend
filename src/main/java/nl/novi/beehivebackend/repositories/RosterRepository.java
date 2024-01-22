package nl.novi.beehivebackend.repositories;

import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RosterRepository extends JpaRepository<Roster, String> {

    List<Roster> findAllByTeam(Team team);

}
