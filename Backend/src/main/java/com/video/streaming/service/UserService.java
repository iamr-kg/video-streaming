package com.video.streaming.service;

import com.video.streaming.dto.AuthUserDto;
import com.video.streaming.dto.ReactionCount;
import com.video.streaming.model.User;

public interface UserService {

     User getCurrentUser();

     long register(AuthUserDto authDetails);

     ReactionCount registerVideoReaction(String videoId, String reaction);

     void subscribeTo(String userId);
     void unSubscribeTo(String userId);
     AuthUserDto validate(String authorization);
}