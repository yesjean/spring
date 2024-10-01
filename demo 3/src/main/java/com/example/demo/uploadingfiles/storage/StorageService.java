package com.example.demo.uploadingfiles.storage;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    default String store(MultipartFile file) {
        try {
            // 저장할 경로 설정
            Path rootLocation = Paths.get("src/main/resources/static");
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            // 파일 이름을 안전하게 정리
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

            // 파일 저장
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName; // 파일의 이름 반환 (절대 경로가 아닌)
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
