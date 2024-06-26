package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.Employee;
import nl.novi.beehivebackend.models.ImageData;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.repositories.EmployeeRepository;
import nl.novi.beehivebackend.repositories.ImageDataRepository;
import nl.novi.beehivebackend.repositories.UserRepository;
import nl.novi.beehivebackend.services.ImageDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageDataController {

    private final ImageDataService imageDataService;
    private final UserRepository userRepository;
    private final ImageDataRepository imageDataRepository;
    private final EmployeeRepository employeeRepository;

    public ImageDataController(ImageDataService imageDataService, UserRepository userRepository, ImageDataRepository imageDataRepository, EmployeeRepository employeeRepository) {
        this.imageDataService = imageDataService;
        this.userRepository = userRepository;
        this.imageDataRepository = imageDataRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam Long employeeId) throws IOException {
        String image = imageDataService.uploadImage(multipartFile, employeeId);
        return ResponseEntity.ok("uploaded file: " + image);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Object> downloadImage(@PathVariable("employeeId") Long employeeId) {
        byte[] image = imageDataService.downloadImage(employeeId);

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + employeeId));
        ImageData dbImageData = imageDataRepository.findById(employee.getImageData().getId()).orElseThrow(() -> new RecordNotFoundException("No image found with employee id: " + employeeId));

        MediaType mediaType = MediaType.valueOf(dbImageData.getType());
        return ResponseEntity.ok().contentType(mediaType).body(image);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteImage(@PathVariable Long id) {
        imageDataService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
