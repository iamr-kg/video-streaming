package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static jakarta.persistence.GenerationType.SEQUENCE;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "video_app_generator")
    @SequenceGenerator(name = "video_app_generator", initialValue = 3, allocationSize = 25)
    @Column(name = "comment_id")
    private long id;
    @Column(name = "text")
    private String text;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id", referencedColumnName = "video_id")
    private Video video;
    private AtomicInteger likeCount;
    private AtomicInteger dislikeCount;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public void incrementLikes() {
        likeCount.incrementAndGet();
    }

    public void decrementLikes() {
        likeCount.decrementAndGet();
    }

    public void incrementDisLikes() {
        dislikeCount.incrementAndGet();
    }

    public void decrementDisLikes() {
        dislikeCount.decrementAndGet();
    }
}
