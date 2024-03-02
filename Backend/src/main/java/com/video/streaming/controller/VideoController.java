package com.video.streaming.controller;

import com.video.streaming.dto.VideoDto;
import com.video.streaming.service.serviceimpl.VideoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("vsa/video")
@RequiredArgsConstructor
public class VideoController {
    Logger log = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PostMapping("/uploadVideo")
    public ResponseEntity<VideoDto> uploadVideo(@RequestParam("file") MultipartFile file){
        VideoDto video;
        try {
            video =  videoService.uploadVideo(file);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File upload error");
        }
        return new ResponseEntity<VideoDto>(video,HttpStatus.CREATED);
    }
    @PostMapping("/uploadThumbnail")
    public ResponseEntity<String> uploadThumbnail(@RequestParam("file") MultipartFile file,@RequestParam("videoId") String videoId){
        String url;
        try {
            url =  videoService.uploadThumbnail(file,videoId);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        return new ResponseEntity<String>(url,HttpStatus.CREATED);
    }

    @PutMapping("/videoDetails")
    public ResponseEntity<VideoDto> videoDetails(@RequestBody VideoDto videoDto){
        VideoDto videoDetails;
        try{
            videoDetails = videoService.saveVideoDetails(videoDto);
    }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        return  new ResponseEntity<VideoDto>(videoDetails,HttpStatus.CREATED);
    }
    @GetMapping("/getVideo/{videoId}")
    public ResponseEntity<VideoDto> getVideoDetails(@PathVariable String videoId){
        VideoDto videoDetails;
        try{
            videoDetails = videoService.getVideoDetails(videoId);
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        return new ResponseEntity<VideoDto>(videoDetails,HttpStatus.CREATED);
    }
}
