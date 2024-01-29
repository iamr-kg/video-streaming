package com.video.streaming.model;

import com.video.streaming.dto.ReactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "video_reaction")
@Getter
@Setter
public class VideoReaction {

    @Id
    @OneToOne()
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "video_reaction")
    private int videoReaction;

}
