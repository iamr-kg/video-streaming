package com.video.streaming.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public String uploadVideo(MultipartFile file);
}
