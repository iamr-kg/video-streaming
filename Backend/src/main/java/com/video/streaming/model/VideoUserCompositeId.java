package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUserCompositeId implements Serializable {
    @ManyToOne()
    @JoinColumn(name = "video_id")
    private Video video;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
