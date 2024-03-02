package com.video.streaming.repository;

import com.video.streaming.model.Subscription;
import com.video.streaming.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
}
