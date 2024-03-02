package com.video.streaming.repository;


import com.video.streaming.model.VideoUserCompositeId;
import com.video.streaming.model.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, VideoUserCompositeId> {
    List<WatchHistory> findByIdUserId(long id);

    @Query("SELECT COUNT(wh.id.video.videoId) FROM WatchHistory wh where wh.id.video.videoId=:videoId")
    long viewCountVideo(@Param("videoId")long videoId);
}
