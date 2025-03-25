package com.waihon.springboot.thymeleaf.jobportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

// This configuration class will map requests for /photos to serve files
// from a directory on our file system.
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR = "photos";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(UPLOAD_DIR, registry);
    }

    private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
        Path path = Paths.get(uploadDir);
        // Map requests starting with "/photos/**" to a file system location file:
        // <absolute path to photos directory>.
        // The ** will match on all sub-directories.
        registry.addResourceHandler("/" + uploadDir + "/**")
                .addResourceLocations("file:" + path.toAbsolutePath() + "/");
    }
}
