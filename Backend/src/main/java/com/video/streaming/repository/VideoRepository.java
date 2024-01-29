package com.video.streaming.repository;

import com.video.streaming.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends JpaRepositoryImplementation<Video,Long>, CrudRepository<Video,Long> {
}
