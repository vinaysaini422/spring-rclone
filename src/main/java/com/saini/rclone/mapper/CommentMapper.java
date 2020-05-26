package com.saini.rclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.saini.rclone.dto.CommentsDto;
import com.saini.rclone.model.Comment;
import com.saini.rclone.model.Post;
import com.saini.rclone.model.User;

@Mapper(componentModel ="spring")
public interface CommentMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(expression = "java(java.time.Instant.now())", target ="createdDate")
	@Mapping(source ="user", target = "user")
	Comment map(CommentsDto commentsDto, Post post, User user);
	
	@Mapping(target ="postId", expression = "java(comment.getPost().getPostId())")
	@Mapping(target ="userName", expression = "java(comment.getUser().getUsername())")
	CommentsDto mapToDto(Comment comment);
	
	
	
}
