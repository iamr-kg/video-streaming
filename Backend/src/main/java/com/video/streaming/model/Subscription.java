package com.video.streaming.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @EmbeddedId
    private SubscriptionId subscriptionId;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

}
