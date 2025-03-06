package src.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class ImageService {

    //после : идет дефолтное значение
    @Value("${app.image.bucket:/home/dm/JavaWorkspace/spring-boot-starter/images}")
    private final String bucket;

    public void upload(String imagePath, InputStream content)  {
        Path fullImagePath = Path.of(bucket, imagePath);
         try (content) {
             //создадим директорию
             Files.createDirectories(fullImagePath.getParent());
             //StandardOpenOption.CREATE - создавать если не существует и
             //StandardOpenOption.TRUNCATE_EXISTING - если существует
             Files.write(fullImagePath, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
    }
}
