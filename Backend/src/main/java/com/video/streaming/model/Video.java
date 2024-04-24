package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import static jakarta.persistence.GenerationType.SEQUENCE;
@Entity
@Table(name="video")
@Embeddable
@Data
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = SEQUENCE,generator = "video_app_generator")
    @SequenceGenerator(name="video_app_generator",initialValue = 3,allocationSize = 25)
    @Column(name="video_id")
    private long videoId;
    @Column(name="file_name")
    private String fileName;
    @Column(name="title")
    private String title;
    @Column(name="description")
    private String description;
    @Column(name="video_url")
    private String videoUrl;
    @Column(name="thumbnail_url")
    private String thumbnailUrl;
    private String tags;
    @Column(name="video_status")
    private String videoStatus;
    @ManyToOne()
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    private User user;
    private LocalDateTime uploadDate;
   // private AtomicLong viewCount;


}
