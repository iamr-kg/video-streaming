package com.video.streaming.repository;

import com.video.streaming.model.User;
import com.video.streaming.model.Video;
import com.video.streaming.model.VideoReaction;
import com.video.streaming.model.VideoUserCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoReactionRepository extends JpaRepository<VideoReaction, VideoUserCompositeId> {
 @Query(value ="SELECT COUNT(vr.videoReaction) FROM VideoReaction vr where vr.videoReaction = 1 and vr.id.video.videoId=:videoId")
 long getLikesCountForVideo(@Param("videoId") long videoId);

 @Query(value ="SELECT COUNT(vr.videoReaction) FROM VideoReaction vr where vr.videoReaction = -1 and vr.id.video.videoId=:videoId")
 long getDisLikesCountForVideo(@Param("videoId") long videoId);

 @Query(value = "SELECT EXISTS(SELECT * FROM video_reaction vr WHERE vr.user_id = :userId and vr.video_id=:videoId)",nativeQuery = true)
 int isLikedByUser(@Param("videoId") long videoId,@Param("userId") long userId);

List<VideoReaction> findAllByIdUserId(Long id);

}
