package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name ="subscription")
@Getter
@Setter
public class Subscription {
    @EmbeddedId
    private SubscriptionId user;

    @Column(name ="is_active")
    private String isActive;

    @Column(name="createdAt")
    private LocalDateTime createdAt;

}
