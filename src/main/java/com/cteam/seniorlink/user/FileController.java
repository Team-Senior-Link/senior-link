package com.cteam.seniorlink.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.default-image}")
    private String defaultImagePath;

    @GetMapping("/profile/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource resource;
            String filenameForHeader;

            if ("default".equals(filename) || filename == null || filename.trim().isEmpty()) {
                resource = new ClassPathResource(defaultImagePath);
                filenameForHeader = "default_profile_image.jpg";
                if (!resource.exists() || !resource.isReadable()) {
                    throw new RuntimeException("기본 프로필 이미지를 찾을 수 없거나 읽을 수 없습니다.");
                }
            } else {
                Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
                resource = new UrlResource(filePath.toUri());
                filenameForHeader = resource.getFilename();
                if (!resource.exists() || !resource.isReadable()) {
                    throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
                }
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filenameForHeader + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일을 읽을 수 없습니다: " + filename, e);
        }
    }
}
