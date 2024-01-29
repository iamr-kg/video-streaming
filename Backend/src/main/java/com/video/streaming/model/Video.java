package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="video")
@Getter
@Setter
@Embeddable
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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Video that)) return false;
        return ((Long)this.getVideoId()).equals(that.getVideoId());
    }
    public int hashCode(){
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((Long)getVideoId()).hashCode();
        return hash;
    }

}
