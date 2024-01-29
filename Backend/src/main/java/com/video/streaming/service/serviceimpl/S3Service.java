package com.video.streaming.service.serviceimpl;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.video.streaming.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service implements StorageService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Override
    public String uploadVideo(MultipartFile multipartfile)  {
        File file = new File(multipartfile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartfile.getBytes());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = this.generateFileName(multipartfile);

        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(FilenameUtils.getExtension(multipartfile.getOriginalFilename()));
            metadata.addUserMetadata("Title", "File Upload - " + fileName);
            metadata.setContentLength(file.length());
            request.setMetadata(metadata);
            s3Client.putObject(request);
            file.delete();
            return fileName;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }


}
