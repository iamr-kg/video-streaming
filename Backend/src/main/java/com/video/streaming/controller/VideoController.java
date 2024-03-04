package com.video.streaming.controller;

import com.video.streaming.dto.CommentDto;
import com.video.streaming.dto.VideoDto;
import com.video.streaming.service.CommentService;
import com.video.streaming.service.serviceimpl.VideoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("vsa/video")
@RequiredArgsConstructor
public class VideoController {
    Logger log = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;
    private final CommentService commentService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/uploadVideo")
    public ResponseEntity<VideoDto> uploadVideo(@RequestParam("file") MultipartFile file) {
        VideoDto video;
        try {
            video = videoService.uploadVideo(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File upload error");
        }
        return new ResponseEntity<VideoDto>(video, HttpStatus.CREATED);
    }

    @PostMapping("/uploadThumbnail")
    public ResponseEntity<String> uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {
        String url;
        try {
            url = videoService.uploadThumbnail(file, videoId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<String>(url, HttpStatus.CREATED);
    }

    @PutMapping("/videoDetails")
    public ResponseEntity<VideoDto> videoDetails(@RequestBody VideoDto videoDto) {
        VideoDto videoDetails;
        try {
            videoDetails = videoService.saveVideoDetails(videoDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<VideoDto>(videoDetails, HttpStatus.CREATED);
    }

    @GetMapping("/getVideo/{videoId}")
    public ResponseEntity<VideoDto> getVideoDetails(@PathVariable String videoId) {
        VideoDto videoDetails;
        try {
            videoDetails = videoService.getVideoDetails(videoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new ResponseEntity<VideoDto>(videoDetails, HttpStatus.CREATED);
    }

    @PostMapping("comment/{videoId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable("videoId") String videoId,@RequestBody CommentDto comment) {
        try {
            CommentDto savedComment = commentService.addComment(Long.parseLong(videoId),comment);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("allComments/{videoId}")
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable("videoId") String videoId){
        try{
            List<CommentDto> savedComment = commentService.getComments(Long.parseLong(videoId));
            return new ResponseEntity<>(savedComment,HttpStatus.CREATED);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @GetMapping("getAllVideos")
    public ResponseEntity<List<VideoDto>> getAllVideos(){
        try{
            List<VideoDto> videoList = videoService.getAllVideos();
            return new ResponseEntity<>(videoList,HttpStatus.CREATED);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
