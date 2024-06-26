package com.video.streaming.service;

import com.video.streaming.dto.AuthUserDto;
import com.video.streaming.dto.ReactionCount;
import com.video.streaming.dto.UserDto;
import com.video.streaming.dto.VideoDto;
import com.video.streaming.model.User;

import java.util.List;

public interface UserService {

     User getCurrentUser();

     UserDto register(AuthUserDto authDetails);

     ReactionCount registerVideoReaction(String videoId, String reaction);

     void subscribeTo(String userId);
     void unSubscribeTo(String userId);
     AuthUserDto validate(String authorization);

    List<VideoDto> getUserHistory();

     List<UserDto> getAllSubscription();

    boolean isSubscribedTo(long userId);

    List<VideoDto> getUserLikedVideos();
}
