package com.video.streaming.controller;

import com.video.streaming.dto.AuthUserDto;
import com.video.streaming.dto.ReactionCount;
import com.video.streaming.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("vsa/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("register")
    public ResponseEntity<Long> registerUser(HttpServletRequest httpServletRequest) {
        try {
            AuthUserDto authDetails = userService.validate(httpServletRequest.getHeader("Authorization"));
            Long userID = userService.register(authDetails);
            return new ResponseEntity<>(userID, HttpStatus.CREATED);
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
    @PostMapping("unSubscribeTo/{userId}")
    public ResponseEntity<Void> unSubscribeTo(@PathVariable("userId") String userId){
        try{
            userService.unSubscribeTo(userId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
