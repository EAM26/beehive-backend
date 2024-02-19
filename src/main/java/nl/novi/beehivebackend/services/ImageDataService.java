package nl.novi.beehivebackend.services;

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

    public String uploadImage(MultipartFile multipartFile, String username) throws IOException {
        User user = userRepository.findByUsername(username);

        ImageData imgData = new ImageData();
        imgData.setName(multipartFile.getName());
        imgData.setType(multipartFile.getContentType());
        imgData.setImageData(ImageUtil.compressImage(multipartFile.getBytes()));
        imgData.setUser(user);

        ImageData savedImage = imageDataRepository.save(imgData);
        user.setImageData(savedImage);
        userRepository.save(user);

        return savedImage.getName();
    }
}
