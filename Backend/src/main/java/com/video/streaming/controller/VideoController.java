package com.video.streaming.controller;

import com.video.streaming.config.S3ClientConfig;
import com.video.streaming.service.StorageService;
import com.video.streaming.service.serviceimpl.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("vsa/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file){
        String url;
        try {
            url =  videoService.uploadVideo(file);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File upload error");
        }
        return new ResponseEntity<String>(url,HttpStatus.CREATED);
    }
}
