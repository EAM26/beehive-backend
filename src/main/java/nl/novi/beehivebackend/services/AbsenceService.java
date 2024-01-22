package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.AbsenceInputDto;
import nl.novi.beehivebackend.dtos.output.AbsenceOutputDto;
import nl.novi.beehivebackend.exceptions.BadRequestException;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Absence;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.repositories.AbsenceRepository;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final EmployeeRepository employeeRepository;

    public AbsenceService(AbsenceRepository absenceRepository, EmployeeRepository employeeRepository) {
        this.absenceRepository = absenceRepository;
        this.employeeRepository = employeeRepository;
    }

    public Iterable<AbsenceOutputDto> getAllAbsences() {
        List<AbsenceOutputDto> absenceOutputDtos = new ArrayList<>();
        for (Absence absence : absenceRepository.findAll()) {
            absenceOutputDtos.add(transferAbsenceToAbsenceOutputDto(absence));
        }
        return absenceOutputDtos;
    }

    public AbsenceOutputDto getSingleAbsence(Long id) {
        Absence absence = absenceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No absence found with id: " + id));
        return transferAbsenceToAbsenceOutputDto(absence);
    }

    public AbsenceOutputDto createAbsence(AbsenceInputDto absenceInputDto) {
        Employee employee = employeeRepository.findById(absenceInputDto.getEmployeeId()).orElseThrow(() -> new RecordNotFoundException("Employee with id " + absenceInputDto.getEmployeeId() + " doesn't exist."));

        Absence absence = transferAbsenceInputDtoToAbsence(absenceInputDto, employee);
        return (transferAbsenceToAbsenceOutputDto(absence));
    }

    public void deleteAbsence(Long id) {
        Absence absence = absenceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No absence found with id:  " + id));
        absenceRepository.delete(absence);
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

    private Absence transferAbsenceInputDtoToAbsence(AbsenceInputDto absenceInputDto, Employee employee) {
        if(isAbsenceOverlap(absenceInputDto, employee)) {
            throw new BadRequestException("Period is overlapping existing absence");
        }
        Absence absence = new Absence();
        absence.setStartDate(absenceInputDto.getStartDate());
        absence.setEndDate(absenceInputDto.getEndDate());
        absence.setEmployee(employee);

        return absenceRepository.save(absence);
    }

//    Helper methods

    public boolean isAbsenceOverlap(AbsenceInputDto absenceInputDto, Employee employee) {
        List<Absence> existingAbsences = absenceRepository.findByEmployeeId(employee.getId());

        for (Absence existingAbsence : existingAbsences) {
            if (!absenceInputDto.getStartDate().isAfter(existingAbsence.getEndDate()) && !absenceInputDto.getEndDate().isBefore(existingAbsence.getStartDate())) {
                return true;
            }
        }
        return false;
    }
}