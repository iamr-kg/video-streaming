package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="watch_history")
public class WatchHistory {
    public WatchHistory(VideoUserCompositeId videoUserCompositeId){
        this.id = videoUserCompositeId;
    }
    @EmbeddedId
    private VideoUserCompositeId id;
    @Column(name="createdAt",columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
}
