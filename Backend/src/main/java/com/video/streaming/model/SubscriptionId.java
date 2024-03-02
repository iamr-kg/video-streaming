package com.video.streaming.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionId implements Serializable {
    @ManyToOne
    @JoinColumn(name="subscriber_id")
    private User subscriberId;

    @ManyToOne
    @JoinColumn(name = "subscribed_to_id")
    private User subscribedToId;



}
