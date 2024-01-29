package com.video.streaming.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class WatchHistory {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne()
    @JoinColumn(name="video_id")
    private Video video;
    @ManyToOne()
    @JoinColumn(name="user_id")
    private  User user;
    @Column(name="createdAt")
    private LocalDateTime createdAt;
}
