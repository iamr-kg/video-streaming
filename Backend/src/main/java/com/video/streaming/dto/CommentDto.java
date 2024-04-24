package com.video.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    public long id;
    public String text;
    public long authorId;
    public String authorName;
    public long videoId;
    public LocalDateTime createdAt;

}
