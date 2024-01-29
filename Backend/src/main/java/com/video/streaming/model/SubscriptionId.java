package com.video.streaming.model;

import com.video.streaming.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Embeddable
@Getter
@Setter
public class SubscriptionId implements Serializable {
    @ManyToOne
    @JoinColumn(name="subscriber_id")
    private User subscriberId;

    @ManyToOne
    @JoinColumn(name = "subscribed_to_id")
    private User subscribedToId;



}
