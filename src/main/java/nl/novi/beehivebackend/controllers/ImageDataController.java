package nl.novi.beehivebackend.controllers;

import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.ImageData;
import nl.novi.beehivebackend.models.User;
import nl.novi.beehivebackend.repositories.ImageDataRepository;
import nl.novi.beehivebackend.repositories.UserRepository;
import nl.novi.beehivebackend.services.ImageDataService;
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

    public ImageDataController(ImageDataService imageDataService, UserRepository userRepository, ImageDataRepository imageDataRepository) {
        this.imageDataService = imageDataService;
        this.userRepository = userRepository;
        this.imageDataRepository = imageDataRepository;
    }

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam String username) throws IOException {
        String image = imageDataService.uploadImage(multipartFile, username);
        return ResponseEntity.ok("uploaded file: " + image);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> downloadImage(@PathVariable("username") String username) {
        byte[] image = imageDataService.downloadImage(username);
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("User not found with name: " + username));
        //        User user = userRepository.findByUsername(username);
        ImageData dbImageData = imageDataRepository.findById(user.getImageData().getId()).orElseThrow(() -> new RecordNotFoundException("No image found with username: " + username));

        MediaType mediaType = MediaType.valueOf(dbImageData.getType());
        return ResponseEntity.ok().contentType(mediaType).body(image);

    }
//    @GetMapping("/{username}")
//    public ResponseEntity<Object> downloadImage2(@PathVariable("username") String username) {
//        ImageData imageData = imageDataService.downloadImage2(username);
//        User user = userRepository.findByUsername(username);
//        ImageData dbImageData = imageDataRepository.findById(user.getImageData().getId()).orElseThrow(() -> new RecordNotFoundException("No image found with username: " + username));
//
//        MediaType mediaType = MediaType.valueOf(dbImageData.getType());
//        return ResponseEntity.ok().contentType(mediaType).body(imageData);
//
//    }
}
