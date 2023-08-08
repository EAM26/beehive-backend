package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ShiftRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class ShiftService {

    private final ModelMapper modelMapper;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;


    public ShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository) {
        this.shiftRepository = shiftRepository;
        this.modelMapper = new ModelMapper();
        this.employeeRepository = employeeRepository;
    }

    public Iterable<ShiftOutputDto> getAllShifts() {
        ArrayList<ShiftOutputDto> shiftOutputDtos = new ArrayList<>();
        for (Shift shift: shiftRepository.findAll()) {
            shiftOutputDtos.add(convertShiftToOutputDto(shift));
        }
        return shiftOutputDtos;
    }

    public ShiftOutputDto getShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        return convertShiftToOutputDto(shift);
    }

    // TODO: 7-7-2023 Check waarom Employee leeg is behalve id als return waarde 
    public ShiftOutputDto createShift(ShiftInputDto shiftInputDto) {
        if(shiftInputDto.getEmployee() != null) {
            employeeRepository.findById(shiftInputDto.getEmployee().getId()).orElseThrow(() -> new RecordNotFoundException("No Employee found with id: " + shiftInputDto.getEmployee().getId()));
        }
        Shift shift = shiftRepository.save(convertInputDtoToShift(shiftInputDto));
        return convertShiftToOutputDto(shift);
    }

    public ShiftOutputDto updateShift(Long id, ShiftInputDto shiftInputDto) {
        shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        if(shiftInputDto.getEmployee() != null) {
            employeeRepository.findById(shiftInputDto.getEmployee().getId()).orElseThrow(() -> new RecordNotFoundException("No Employee found with id: " + shiftInputDto.getEmployee().getId()));
        }
        Shift shift = convertInputDtoToShift(shiftInputDto);
        shift.setId(id);
        shiftRepository.save(shift);
        return convertShiftToOutputDto(shift);
    }

    public void deleteShift(Long id) {
        try {
            shiftRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("No shift found with id: " + id);
        }
    }

    


    
//    Conversion modelmapper methods

    private ShiftOutputDto convertShiftToOutputDto(Shift shift) {
        return modelMapper.map(shift, ShiftOutputDto.class);
    }

    private Shift convertInputDtoToShift(ShiftInputDto shiftInputDto) {
        return modelMapper.map(shiftInputDto, Shift.class);
    }

}
