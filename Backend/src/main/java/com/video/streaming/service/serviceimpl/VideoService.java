package com.video.streaming.service.serviceimpl;

import com.amazonaws.services.s3.AmazonS3;
import com.video.streaming.dto.ReactionCount;
import com.video.streaming.dto.UserDto;
import com.video.streaming.dto.VideoDto;
import com.video.streaming.model.*;
import com.video.streaming.repository.UserRepository;
import com.video.streaming.repository.VideoReactionRepository;
import com.video.streaming.repository.VideoRepository;
import com.video.streaming.repository.WatchHistoryRepository;
import com.video.streaming.service.StorageService;
import com.video.streaming.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoService  {
    private final StorageService storageService;
    private final VideoRepository videoRepository;
    private final AmazonS3 s3Client;
    private final ModelMapper modelMapper;
    private final WatchHistoryRepository watchHistoryRepository;
    private final VideoReactionRepository videoReactionRepository;
    private final UserRepository userRepository;
    @Value("${aws.bucket.name}")
    private String bucketName;

    public User getCurrentUser() {
        String sub = ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getCredentials())).getClaim("sub");

        return userRepository.findBySub(sub)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub - " + sub));
    }
    public VideoDto uploadVideo(MultipartFile file) {
        User user = getCurrentUser();
        Video video = new Video();
        video.setUser(user);
        video.setFileName(storageService.uploadFile(file));
        video.setVideoUrl(s3Client.getUrl(bucketName,video.getFileName()).toString());
        Video savedVideo = this.saveVideoMetaDataDB(video);
        VideoDto videoDetails = new VideoDto();
        this.modelMapper.map(savedVideo,videoDetails);
        return videoDetails;
    }

    private Video saveVideoMetaDataDB(Video video) {
        return videoRepository.save(video);
    }

    public VideoDto saveVideoDetails(VideoDto videoDto) {

        //find the video by id
        Video savedVideo = findVideoById(videoDto.getVideoId());
        //mad all fields to video
        ModelMapper videoMapper = new ModelMapper();
        TypeMap<VideoDto,Video> skipMappings = videoMapper.createTypeMap(VideoDto.class, Video.class);
        skipMappings.addMappings(m -> m.skip(Video::setVideoUrl));
        skipMappings.addMappings(m -> m.skip(Video::setThumbnailUrl));
        skipMappings.addMappings(m -> m.skip(Video::setFileName));

        videoMapper.map(videoDto,savedVideo);
        //
        Video repoInstance = videoRepository.save(savedVideo);

        videoDto.setVideoUrl(repoInstance.getVideoUrl());
        videoDto.setFileName(repoInstance.getFileName());

        return videoDto;

    }
    public Video  findVideoById(Long id){
        //find the video by id
        return videoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("cannot find video by id-"+ id));
    }
    public String uploadThumbnail(MultipartFile file,String videoId) throws Exception {
        Video savedVideo = findVideoById(Long.parseLong(videoId));
        String fileName = storageService.uploadFile(file);
        savedVideo.setThumbnailUrl(s3Client.getUrl(bucketName,fileName).toString());

        Video repoVideo = videoRepository.save(savedVideo);

        return repoVideo.getThumbnailUrl();
    }

    public VideoDto getVideoDetails(String videoId){
        Video savedVideo = findVideoById(Long.parseLong(videoId));
        User user = getCurrentUser();
        addWatchHistory(savedVideo,user);
        //savedVideo.incrementViewCount();
        VideoDto videoDetails = new VideoDto();
        ModelMapper videoMapper = new ModelMapper();
        videoMapper.map(savedVideo,videoDetails);
        videoDetails.setUser(modelMapper.map(savedVideo.getUser(), UserDto.class));
        videoDetails.setViewCount(watchHistoryRepository.viewCountVideo(Long.parseLong(videoId)));
        return videoDetails;
    }

    public void addWatchHistory(Video video,User user){
        VideoUserCompositeId primary = new VideoUserCompositeId(video,user);

        WatchHistory wh = new WatchHistory(primary);
        watchHistoryRepository.save(wh);
    }

    public List<VideoDto> getAllVideos() {
        return this.videoRepository.findAll().stream().map(video-> {
            VideoDto videoDetails = modelMapper.map(video,VideoDto.class);
            videoDetails.setViewCount(watchHistoryRepository.viewCountVideo(videoDetails.getVideoId()));
            return videoDetails;
        }).toList();
    }

    public ReactionCount getVideoReactionCount(long videoId) {
        ReactionCount reactionCounts = new ReactionCount();
        videoReactionRepository.findById(new VideoUserCompositeId(findVideoById(videoId),getCurrentUser())).ifPresent(savedReaction -> {
            reactionCounts.setReactionByUser(savedReaction.getVideoReaction());
        });
        reactionCounts.setLikeCount(videoReactionRepository.getLikesCountForVideo(videoId));
        reactionCounts.setDisLikeCount(videoReactionRepository.getDisLikesCountForVideo(videoId));
        System.out.println(videoReactionRepository.isLikedByUser(videoId,getCurrentUser().getId()));
        return reactionCounts;
    }

    public ReactionCount likeVideo(long videoId) {
       User user = getCurrentUser();
       VideoUserCompositeId primary = new VideoUserCompositeId(findVideoById(videoId),user);

       Optional<VideoReaction> reactionOptional= videoReactionRepository.findById(primary);

       if(reactionOptional.isEmpty()){
           videoReactionRepository.save(new VideoReaction(primary,1));
           return getVideoReactionCount(videoId);
       }
        VideoReaction reaction = reactionOptional.get();
        if(reaction.getVideoReaction() == 1){
            reaction.setVideoReaction(0);
        }else{
            reaction.setVideoReaction(1);
        }
        videoReactionRepository.save(reaction);
        return getVideoReactionCount(videoId);
    }

    public ReactionCount disLikeVideo(long videoId) {
        User user = getCurrentUser();
        VideoUserCompositeId primary = new VideoUserCompositeId(findVideoById(videoId),user);

        Optional<VideoReaction> reactionOptional= videoReactionRepository.findById(primary);

        if(reactionOptional.isEmpty()){
            videoReactionRepository.save(new VideoReaction(primary,-1));
            return getVideoReactionCount(videoId);
        }

        VideoReaction reaction = reactionOptional.get();
        if(reaction.getVideoReaction() == -1){
            reaction.setVideoReaction(0);
        }else{
            reaction.setVideoReaction(-1);
        }

        videoReactionRepository.save(reaction);
        return getVideoReactionCount(videoId);
    }
}
