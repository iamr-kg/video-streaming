package com.video.streaming.model;

import com.video.streaming.repository.VideoRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;


@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user")
@Embeddable
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = SEQUENCE,generator = "video_app_generator")
    @SequenceGenerator(name="video_app_generator",initialValue = 3,allocationSize = 25)
    @Column(name="user_id")
    private long id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="user_name")
    private String userName;
    @Column(name ="email_address")
    private String emailAddress;
    @Column(name="role")
    private String role;
    @Column(name="createdAt")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user")
    private List<Video> video;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Video that)) return false;
        return ((Long)this.getId()).equals(that.getVideoId());
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((Long)getId()).hashCode();
        return hash;
    }


}
