package com.video.streaming.controller;

import com.video.streaming.dto.AuthUserDto;
import com.video.streaming.dto.ReactionCount;
import com.video.streaming.dto.UserDto;
import com.video.streaming.dto.VideoDto;
import com.video.streaming.model.User;
import com.video.streaming.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("vsa/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("welcome")
    public String welcome(){
        return "Welcome to VSA";
    }
    @GetMapping("register")
    public ResponseEntity<UserDto> registerUser(HttpServletRequest httpServletRequest) {
        try {
            AuthUserDto authDetails = userService.validate(httpServletRequest.getHeader("Authorization"));
            UserDto user = userService.register(authDetails);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @PostMapping("videoReaction/{videoId}/{videoReaction}")
    public ResponseEntity<ReactionCount> videoReaction(@PathVariable("videoId")String videoId,
                                                        @PathVariable("videoReaction") String videoReaction){
        try{
            ReactionCount rc = userService.registerVideoReaction(videoId,videoReaction);
            return new ResponseEntity<>(rc, HttpStatus.CREATED);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @PostMapping("subscribeTo/{userId}")
    public ResponseEntity<Void> subscribeTo(@PathVariable("userId") String userId){
        try{
            userService.subscribeTo(userId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @GetMapping("getAllSubscriptions")
    public ResponseEntity<List<UserDto>> subscribeTo(){
        try{
            List<UserDto> subs = userService.getAllSubscription();
            return new ResponseEntity<>(subs,HttpStatus.CREATED);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    @PostMapping("unSubscribeTo/{userId}")
    public ResponseEntity<Void> unSubscribeTo(@PathVariable("userId") String userId){
        try{
            userService.unSubscribeTo(userId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }


    @GetMapping("isSubscribedToUser/{userId}")
    public ResponseEntity<String> isSubscribed(@PathVariable("userId")String userId){
        try{
            boolean subscription = userService.isSubscribedTo(Long.parseLong(userId));
            return new ResponseEntity<>(((Boolean)subscription).toString(),HttpStatus.CREATED);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @GetMapping("userHistory")
    public ResponseEntity<List<VideoDto>> getUserHistory(){
        try{
            List<VideoDto> userHistory = userService.getUserHistory();
            return new ResponseEntity<>(userHistory,HttpStatus.CREATED);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @GetMapping("likedVideos")
    public ResponseEntity<List<VideoDto>> getAllLikedVideos(){
        try{
            List<VideoDto> liked = userService.getUserLikedVideos();
            return new ResponseEntity<>(liked,HttpStatus.CREATED);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

}
