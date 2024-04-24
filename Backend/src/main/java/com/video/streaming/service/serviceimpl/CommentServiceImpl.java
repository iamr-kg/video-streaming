package com.video.streaming.service.serviceimpl;

import com.video.streaming.dto.CommentDto;
import com.video.streaming.model.Comment;
import com.video.streaming.model.User;
import com.video.streaming.repository.CommentRepository;
import com.video.streaming.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final  ModelMapper modelMapper;
    @Override
    public CommentDto addComment(CommentDto comment) {
        User user = userService.getCurrentUser();
        Comment commentToSave = modelMapper.map(comment,Comment.class);
        commentToSave.setUser(user);
        commentToSave.setVideo(videoService.findVideoById(comment.getVideoId()));
        commentToSave.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentToSave);

        comment = modelMapper.map(savedComment,CommentDto.class);
        comment.setVideoId(savedComment.getVideo().getVideoId());
        comment.setAuthorId(savedComment.getUser().getId());
        comment.setAuthorName(savedComment.getUser().getFullName());
        return comment;
    }

    @Override
    public List<CommentDto> getComments(long videoId) {
        List<Comment> dbCommentsList = commentRepository.findByVideoVideoId(videoId);

        return dbCommentsList.stream().map((comment)->{
            CommentDto savedDetails = modelMapper.map(comment, CommentDto.class);
            savedDetails.setAuthorId(comment.getId());
            savedDetails.setAuthorName(comment.getUser().getFullName());
           // userRepository.findById(savedDetails.getAuthorId()).ifPresentOrElse((user)-> savedDetails.setAuthorName(user.getFullName()),()->savedDetails.setAuthorName("User")
           return savedDetails;
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
