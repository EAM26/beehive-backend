package nl.novi.beehivebackend.services;

import jakarta.transaction.Transactional;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.ImageData;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ImageDataRepository;
import nl.novi.beehivebackend.repositories.UserRepository;
import nl.novi.beehivebackend.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageDataService {
    private final ImageDataRepository imageDataRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    public ImageDataService(ImageDataRepository imageDataRepository, UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.imageDataRepository = imageDataRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

@Transactional
    public String uploadImage(MultipartFile multipartFile, Long employeeId) throws IOException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + employeeId));
        ImageData imgData;
        Optional<ImageData> existingImageDataOpt = imageDataRepository.findByEmployee(employee);
        if(existingImageDataOpt.isPresent()) {
            imgData = imageDataRepository.findByEmployee(employee).orElseThrow(() -> new RecordNotFoundException("No file found with employee id: " + employee.getId()));
        } else {
            imgData = new ImageData();
        }
        imgData.setName(multipartFile.getName());
        imgData.setType(multipartFile.getContentType());
        imgData.setImageData(ImageUtil.compressImage(multipartFile.getBytes()));

        imgData.setEmployee(employee);

        ImageData savedImage = imageDataRepository.save(imgData);

        employee.setImageData(savedImage);

        employeeRepository.save(employee);

        return savedImage.getName();
    }

    public byte[] downloadImage(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + employeeId));
//        ImageData imageData = imageDataRepository.findByEmployee(employee).orElseThrow(() -> new RecordNotFoundException("No image found for employee with id: " + employeeId));
        ImageData imageData;
        if(employee.getImageData() != null) {
            imageData = employee.getImageData();
            return ImageUtil.decompressImage(imageData.getImageData());
        }
        throw new RecordNotFoundException("No image found for employee with id: " + employeeId);

    }

    public void deleteImage(Long id) {
        ImageData imageData = imageDataRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("No image found with id: " + id));
        imageDataRepository.delete(imageData);
    }
}
