package com.cloudcomputing.cloudcomputing.imageService;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {

    String getImageUrl(String name);

    String save(MultipartFile file) throws IOException;

    void delete(String imgUrl) throws IOException;

    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    default String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + originalFileName +getExtension(originalFileName);
    }



}
