package com.video.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private long videoId;
    private String fileName;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private String tags;
    private String videoStatus;
    private long viewCount;
    public UserDto user;
    public LocalDateTime uploadDate;
}
