package nl.novi.beehivebackend.services;

import nl.novi.beehivebackend.dtos.input.ShiftInputDto;
import nl.novi.beehivebackend.dtos.output.ShiftOutputDto;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Shift;
import nl.novi.beehivebackend.repositories.ShiftRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@Service
public class ShiftService {

    private ModelMapper modelMapper;
    private final ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
        this.modelMapper = new ModelMapper();
    }

    public Iterable<ShiftOutputDto> getAllShifts() {
        ArrayList<ShiftOutputDto> shiftOutputDtos = new ArrayList<>();
        for (Shift shift: shiftRepository.findAll()) {
            shiftOutputDtos.add(convertShiftToDto(shift));
        }
        return shiftOutputDtos;
    }

    public ShiftOutputDto getShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        return convertShiftToDto(shift);
    }

    public ShiftOutputDto createShift(ShiftInputDto shiftInputDto) {
        Shift shift = shiftRepository.save(convertDtoToShift(shiftInputDto));
        return convertShiftToDto(shift);
    }

    public ShiftOutputDto updateShift(Long id, ShiftInputDto shiftInputDto) {
        shiftRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No shift found with id: " + id));
        Shift shift = convertDtoToShift(shiftInputDto);
        shift.setId(id);
        shiftRepository.save(shift);
        return convertShiftToDto(shift);
    }

    public void deleteShift(Long id) {
        try {
            shiftRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("No shift found with id: " + id);
        }
    }




//    Conversion modelmapper methods

    private ShiftOutputDto convertShiftToDto(Shift shift) {
        return modelMapper.map(shift, ShiftOutputDto.class);
    }

    private Shift convertDtoToShift(ShiftInputDto shiftInputDto) {
        return modelMapper.map(shiftInputDto, Shift.class);
    }

}
