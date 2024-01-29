package com.video.streaming.service.serviceimpl;

import com.amazonaws.services.s3.AmazonS3;
import com.video.streaming.dto.VideoStatus;
import com.video.streaming.model.Video;
import com.video.streaming.repository.VideoRepository;
import com.video.streaming.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor
public class VideoService  {
    private final StorageService storageService;
    private final VideoRepository videoRepository;
    private final AmazonS3 s3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;
    public String uploadVideo(MultipartFile file) {
        String fileName =  storageService.uploadVideo(file);
        String url =  s3Client.getUrl(bucketName,fileName).toString();
        this.saveVideoMetaDataDB(fileName, url);
        return "uploaded";
    }

    private void saveVideoMetaDataDB(String fileName, String url) {
        Video video = new Video();
        video.setVideoStatus(VideoStatus.PUBLIC.toString());
        video.setVideoUrl(url);
        video.setTitle(fileName);
        videoRepository.save(video);
    }
}
