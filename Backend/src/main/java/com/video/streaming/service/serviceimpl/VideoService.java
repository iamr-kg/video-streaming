package com.video.streaming.service.serviceimpl;

import com.amazonaws.services.s3.AmazonS3;
import com.video.streaming.dto.VideoDto;
import com.video.streaming.model.User;
import com.video.streaming.model.Video;
import com.video.streaming.model.VideoUserCompositeId;
import com.video.streaming.model.WatchHistory;
import com.video.streaming.repository.VideoRepository;
import com.video.streaming.repository.WatchHistoryRepository;
import com.video.streaming.service.StorageService;
import com.video.streaming.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor
public class VideoService  {
    private final StorageService storageService;
    private final VideoRepository videoRepository;
    private final AmazonS3 s3Client;
    private final ModelMapper modelMapper;
    private final WatchHistoryRepository watchHistoryRepository;
    private final UserService userService;
    @Value("${aws.bucket.name}")
    private String bucketName;
    public VideoDto uploadVideo(MultipartFile file) {
        Video video = new Video();
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
        User user = userService.getCurrentUser();
        addWatchHistory(savedVideo,user);
        VideoDto videoDetails = new VideoDto();
        modelMapper.map(savedVideo,videoDetails);
        videoDetails.setViewCount(watchHistoryRepository.viewCountVideo(Long.parseLong(videoId)));
        return videoDetails;
    }

    public void addWatchHistory(Video video,User user){
        VideoUserCompositeId primary = new VideoUserCompositeId(video,user);

        WatchHistory wh = new WatchHistory(primary);
        watchHistoryRepository.save(wh);
    }
}
