package com.video.streaming.service;

import com.video.streaming.dto.CommentDto;

import java.util.List;

public interface CommentService {
    public CommentDto addComment(long videoId,CommentDto comment);

    List<CommentDto> getComments(long videoId);
}
