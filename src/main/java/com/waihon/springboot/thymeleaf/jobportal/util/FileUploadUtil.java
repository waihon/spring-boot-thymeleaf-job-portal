package com.waihon.springboot.thymeleaf.jobportal.util;

import com.waihon.springboot.thymeleaf.jobportal.exception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)
            throws FileUploadException {
        if (multipartFile.isEmpty()) {
            throw new FileUploadException("Image file '" + fileName + "' is empty or no longer exists. Please try again.");
        }

        if (fileName == null || fileName.equals("")) {
            return;
        }

        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path path = uploadPath.resolve(fileName);
                System.out.println("File path: " + path);
                System.out.println("File name: " + fileName);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ioe) {
            throw new FileUploadException("Could not save image file '" + fileName + "'. Please try again.");
        }
    }

}
