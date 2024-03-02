package com.video.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
