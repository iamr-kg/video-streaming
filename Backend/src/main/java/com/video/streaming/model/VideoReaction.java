package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video_reaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoReaction {
    @EmbeddedId
    private VideoUserCompositeId id;
    @Column(name = "video_reaction")
    private int videoReaction;

}
