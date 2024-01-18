package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.models.Absence;
import nl.novi.beehivebackend.repositories.AbsenceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepository;

    public AbsenceService(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    public Iterable<AbsenceOutputDto> getAllAbsences() {
        List<AbsenceOutputDto> absenceOutputDtos = new ArrayList<>();
        for(Absence absence: absenceRepository.findAll()) {
            absenceOutputDtos.add(transferAbsenceToAbsenceOutputDto(absence));
        }
        return absenceOutputDtos;
    }


    private AbsenceOutputDto transferAbsenceToAbsenceOutputDto(Absence absence) {
        AbsenceOutputDto absenceOutputDto = new AbsenceOutputDto();
        absenceOutputDto.setId(absence.getId());
        absenceOutputDto.setStartDate(absence.getStartDate());
        absenceOutputDto.setEndDate(absence.getEndDate());
        absenceOutputDto.setEmployeeId(absence.getEmployee().getId());
        absenceOutputDto.setEmployeeShortName(absence.getEmployee().getShortName());

        return absenceOutputDto;
    }
}
