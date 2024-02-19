package nl.novi.beehivebackend.services;

import jakarta.transaction.Transactional;
import nl.novi.beehivebackend.exceptions.RecordNotFoundException;
import nl.novi.beehivebackend.models.ImageData;
import nl.novi.beehivebackend.models.User;
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


    public ImageDataService(ImageDataRepository imageDataRepository, UserRepository userRepository) {
        this.imageDataRepository = imageDataRepository;
        this.userRepository = userRepository;
    }

@Transactional
    public String uploadImage(MultipartFile multipartFile, String username) throws IOException {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + username));
//        ImageData imgData;
//        if (imageDataRepository.existsByUser(user)) {
//            imgData = imageDataRepository.findByUser(user).orElseThrow(() -> new RecordNotFoundException("No image found with username: " + user.getUsername()));
//
//        } else {
//            imgData = new ImageData();
//        }


        ImageData imgData;
        Optional<ImageData> existingImageDataOpt = imageDataRepository.findByUser(user);
        if(existingImageDataOpt.isPresent()) {
            imgData = imageDataRepository.findByUser(user).orElseThrow(() -> new RecordNotFoundException("No file found with username: " + user.getUsername()));
        } else {
            imgData = new ImageData();
        }
        imgData.setName(multipartFile.getName());
        imgData.setType(multipartFile.getContentType());
        imgData.setImageData(ImageUtil.compressImage(multipartFile.getBytes()));
        imgData.setUser(user);

        ImageData savedImage = imageDataRepository.save(imgData);
        user.setImageData(savedImage);
        userRepository.save(user);

        return savedImage.getName();
    }

    public byte[] downloadImage(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new RecordNotFoundException("No user found with name: " + username));
        ImageData imageData = user.getImageData();
        return ImageUtil.decompressImage(imageData.getImageData());

    }
//    public ImageData downloadImage2(String username) {
//       User user =  userRepository.findByUsername(username);
//       ImageData imageData = user.getImageData();
//       return imageData;
//    }
}
