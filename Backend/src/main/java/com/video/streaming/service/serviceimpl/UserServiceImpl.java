package com.video.streaming.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.streaming.dto.AuthUserDto;
import com.video.streaming.dto.ReactionCount;
import com.video.streaming.dto.ReactionType;
import com.video.streaming.model.*;
import com.video.streaming.repository.SubscriptionRepository;
import com.video.streaming.repository.UserRepository;
import com.video.streaming.repository.VideoReactionRepository;
import com.video.streaming.repository.VideoRepository;
import com.video.streaming.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${auth0.userInfoEndpoint}")
    private String userInfoEndpoint;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final VideoReactionRepository videoReactionRepository;
    private final SubscriptionRepository subscriptionRepository;
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
    public long register(AuthUserDto authDetails) {
        try {
            Optional<User> userIfPresent = userRepository.findByEmailAddress(authDetails.getEmail());
            if (userIfPresent.isPresent()) return userIfPresent.get().getId();
            var user = new User();
            user.setSub(authDetails.getSub());
            user.setEmailAddress(authDetails.getEmail());
            user.setFirstName(authDetails.getGivenName());
            user.setLastName(authDetails.getFamilyName());
            user.setFullName(authDetails.getName());
            user.setPicture(authDetails.getPicture());
            User savedUser = userRepository.save(user);
            return savedUser.getId();
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
        long likeCount = videoReactionRepository.getLikesCountForVideo(Long.parseLong(videoId));
        long disLikeCount = videoReactionRepository.getDisLikesCountForVideo(Long.parseLong(videoId));

        return new ReactionCount(likeCount,disLikeCount);
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

    @Override
    public void unSubscribeTo(String userId) {
        User loginUser = getCurrentUser();
        Optional<User> subscriptionUserOptional = userRepository.findById(Long.parseLong(userId));
        if(subscriptionUserOptional.isEmpty()){
            throw new IllegalArgumentException("User cannot be found");
        }
        SubscriptionId primary = new SubscriptionId(loginUser,subscriptionUserOptional.get());
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(primary);
        subscriptionOptional.ifPresentOrElse((subscription -> {
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
}
