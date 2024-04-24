package com.video.streaming.repository;

import com.video.streaming.model.Subscription;
import com.video.streaming.model.SubscriptionId;
import com.video.streaming.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    List<Subscription> findAllBySubscriptionIdSubscriberId(User user);

    @Query(value="SELECT EXISTS(SELECT * FROM subscription s WHERE s.subscriber_id = :userId AND subscribed_to_id = :videoUserId and is_active = 1)",nativeQuery = true)
    int isActivelySubscribedTo(@Param("userId")String userId,@Param("videoUserId")String videoUserId);
}
