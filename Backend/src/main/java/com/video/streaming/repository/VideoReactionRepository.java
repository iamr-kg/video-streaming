package com.video.streaming.repository;

import com.video.streaming.model.VideoReaction;
import com.video.streaming.model.VideoUserCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoReactionRepository extends JpaRepository<VideoReaction, VideoUserCompositeId> {
 @Query(value ="SELECT COUNT(vr.videoReaction) FROM VideoReaction vr where vr.videoReaction = 1 and vr.id.video.videoId=:videoId")
 long getLikesCountForVideo(@Param("videoId") long videoId);

 @Query(value ="SELECT COUNT(vr.videoReaction) FROM VideoReaction vr where vr.videoReaction = -1 and vr.id.video.videoId=:videoId")
 long getDisLikesCountForVideo(@Param("videoId") long videoId);

}
