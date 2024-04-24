package com.video.streaming.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.streaming.dto.*;
import com.video.streaming.model.*;
import com.video.streaming.repository.*;
import com.video.streaming.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${auth0.userInfoEndpoint}")
    private String userInfoEndpoint;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final VideoReactionRepository videoReactionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final ModelMapper modelMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    @Override
    public User getCurrentUser() {
        String sub = ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getCredentials())).getClaim("sub");

        return userRepository.findBySub(sub)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub - " + sub));
    }
    @Override
    public UserDto register(AuthUserDto authDetails) {
        try {
            Optional<User> userIfPresent = userRepository.findByEmailAddress(authDetails.getEmail());
            if (userIfPresent.isPresent()) return modelMapper.map(userIfPresent.get(),UserDto.class);
            var user = new User();
            user.setSub(authDetails.getSub());
            user.setEmailAddress(authDetails.getEmail());
            user.setFirstName(authDetails.getGivenName());
            user.setLastName(authDetails.getFamilyName());
            user.setFullName(authDetails.getName());
            user.setPicture(authDetails.getPicture());
            User savedUser = userRepository.save(user);
            log.atError().log("Registered user to DB");
            return modelMapper.map(savedUser,UserDto.class);
        }catch(Exception e){
            throw new RuntimeException("Exception Occurred when registering user");
        }
    }

    @Override
    public ReactionCount registerVideoReaction(String videoId,String reaction) {
        Optional<Video> videoIf = videoRepository.findById(Long.parseLong(videoId));
        User user = getCurrentUser();
        if(videoIf.isEmpty()){
            throw new RuntimeException("Not Present");
        }
        VideoUserCompositeId primary = new VideoUserCompositeId(videoIf.get(),user);
        Optional<VideoReaction> reactionIf = videoReactionRepository.findById(primary);
        VideoReaction reactionObject;
        if(reactionIf.isEmpty() || reactionIf.get().getVideoReaction() != ReactionType.valueOf(reaction).getReactionNumber()){
            reactionObject = new VideoReaction(primary, ReactionType.valueOf(reaction).getReactionNumber());
            videoReactionRepository.save(reactionObject);
        }
        ReactionCount reactionCount = new ReactionCount();
         reactionCount.setLikeCount(videoReactionRepository.getLikesCountForVideo(Long.parseLong(videoId)));
        reactionCount.setDisLikeCount(videoReactionRepository.getDisLikesCountForVideo(Long.parseLong(videoId)));
        return reactionCount;
    }

    @Override
    public void subscribeTo(String userId) {

        User loginUser = getCurrentUser();
        Optional<User> subscriptionUserOptional = userRepository.findById(Long.parseLong(userId));
        subscriptionUserOptional.ifPresentOrElse((user)->{
            SubscriptionId primary = new SubscriptionId(loginUser,user);
            subscriptionRepository.save(new Subscription(primary,true, LocalDateTime.now()));
        },
                ()-> {throw new IllegalArgumentException("User cannot be found");});
    }

    public Optional<Subscription> getSubscriptionDetails(String userId){
        User loginUser = getCurrentUser();
        Optional<User> subscriptionUserOptional = userRepository.findById(Long.parseLong(userId));
        if(subscriptionUserOptional.isEmpty()){
            throw new IllegalArgumentException("User cannot be found");
        }
        SubscriptionId primary = new SubscriptionId(loginUser,subscriptionUserOptional.get());
        return subscriptionRepository.findById(primary);
    }
    @Override
    public void unSubscribeTo(String userId) {

        getSubscriptionDetails(userId).ifPresentOrElse((subscription -> {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
        }),()-> {throw new IllegalArgumentException("Subscription cannot be found");});
    }

    public AuthUserDto validate(String authorization){
        if(!authorization.startsWith("Bearer "))
            throw new RuntimeException("Invalid Access Token");
        HttpRequest httpRequest =  HttpRequest.newBuilder(URI.create(userInfoEndpoint)).GET()
                .setHeader("Authorization",authorization)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(body,AuthUserDto.class);
        }catch(Exception e){
            throw new RuntimeException("Exception Occurred when validating user");
        }
    }

    @Override
    public List<VideoDto> getUserHistory() {
        User user = getCurrentUser();
        List<WatchHistory> userHistory = watchHistoryRepository.findByIdUserId(user.getId());

        if(userHistory.isEmpty()){
            return new ArrayList<>();
        }

        return userHistory.stream().map(watchHistory -> {
            Video watchHistoryVideo = watchHistory.getId().getVideo();
            return modelMapper.map(watchHistoryVideo,VideoDto.class);
        }).toList();
    }

    @Override
    public List<UserDto> getAllSubscription() {
        User user = getCurrentUser();
        List<Subscription> subscriptions =  subscriptionRepository.findAllBySubscriptionIdSubscriberId(user);

        if(subscriptions.isEmpty()){
            return null;
        }
        return subscriptions.stream().map(subscription -> modelMapper.map(subscription.getSubscriptionId().getSubscribedToId(), UserDto.class)).toList();
    }

    @Override
    public boolean isSubscribedTo(long userId) {
        User loginUser = getCurrentUser();

        int subscription = subscriptionRepository.isActivelySubscribedTo(Long.toString(loginUser.getId()),Long.toString(userId));
        if(subscription >0) return true;
        return false;
    }

    @Override
    public List<VideoDto> getUserLikedVideos() {
        User user = getCurrentUser();
        List<VideoReaction> likedVideos =  videoReactionRepository.findAllByIdUserId(user.getId());
        if(likedVideos.isEmpty()){
            return null;
        }
        return likedVideos.stream().map(reaction -> modelMapper.map(reaction.getId().getVideo(), VideoDto.class)).collect(Collectors.toList());
    }
}
