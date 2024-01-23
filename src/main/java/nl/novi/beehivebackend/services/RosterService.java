package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.output.RosterOutputDto;
import nl.novi.beehivebackend.dtos.output.RosterOutputDtoExShift;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.models.Roster;
import nl.novi.beehivebackend.repositories.RosterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class RosterService {

    private final RosterRepository rosterRepository;

    public RosterService(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    public RosterOutputDto getSingleRoster(String roserName) {
        Roster roster = rosterRepository.findById(roserName).orElseThrow(() -> new BadRequestException("No Roster found with name: " + roserName));

        return transferRosterToOutputDto(roster);
    }

    public void deleteRoster(String rosterName) {
        Roster roster = rosterRepository.findById(rosterName).orElseThrow(() -> new BadRequestException("No roster found with name: " + rosterName));
        if (roster.getShifts() != null && !roster.getShifts().isEmpty()) {
            throw new BadRequestException("Roster has Shifts");
        }
        rosterRepository.delete(roster);
    }

    private RosterOutputDto transferRosterToOutputDto(Roster roster) {
        RosterOutputDto rosterOutputDto = new RosterOutputDto();
        rosterOutputDto.setName(roster.getName());
        rosterOutputDto.setShifts(roster.getShifts());
        return rosterOutputDto;
    }
}
