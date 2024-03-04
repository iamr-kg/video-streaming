package com.video.streaming.service.serviceimpl;

import com.video.streaming.dto.CommentDto;
import com.video.streaming.model.Comment;
import com.video.streaming.model.User;
import com.video.streaming.repository.CommentRepository;
import com.video.streaming.service.CommentService;
import com.video.streaming.service.UserService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Service
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final VideoService videoService;
    private final CommentRepository commentRepository;

    private final  ModelMapper modelMapper;
    @Override
    public CommentDto addComment(long videoId,CommentDto comment) {
        User user = userService.getCurrentUser();
        Comment commentToSave = modelMapper.map(comment,Comment.class);
        commentToSave.setUser(user);
        commentToSave.setVideo(videoService.findVideoById(videoId));
        commentToSave.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentToSave);
        return modelMapper.map(savedComment,CommentDto.class);
    }

    @Override
    public List<CommentDto> getComments(long videoId) {
        List<Comment> dbCommentsList = commentRepository.findByVideoVideoId(videoId);

        return dbCommentsList.stream().map((comment)-> modelMapper.map(comment, CommentDto.class)).collect(Collectors.toCollection(ArrayList::new));
    }
}
