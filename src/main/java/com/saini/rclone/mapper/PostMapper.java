package com.saini.rclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.saini.rclone.dto.PostRequest;
import com.saini.rclone.dto.PostResponse;
import com.saini.rclone.model.Post;
import com.saini.rclone.model.Subreddit;
import com.saini.rclone.model.User;
import com.saini.rclone.repositories.CommentRepository;




@Mapper(componentModel = "spring")
public abstract class PostMapper {

	@Autowired
	private CommentRepository commentRepository;
	/*
	 * @Autowired private VoteRepository voteRepository;
	 */
	/*
	 * @Autowired private AuthService authService;
	 */

	@Mapping(expression = "java(java.time.Instant.now())", target = "createdDate")
	@Mapping(source = "postRequest.description", target = "description")
	@Mapping(source = "subreddit", target = "subreddit")
	@Mapping(target = "voteCount", constant = "0")
	public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

	@Mapping(source = "postId", target = "id")
	@Mapping(source ="subreddit.name" , target = "subredditName")
	@Mapping(source = "user.username", target = "userName")
	@Mapping(expression = "java(commentCount(post))", target = "commentCount")
	//@Mapping(expression = "java(getDuration(post))", target = "duration")
	public abstract PostResponse mapToDto(Post post);
	
	Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

	/*
	 * String getDuration(Post post) { return
	 * TimeAgo.using(post.getCreatedDate().toEpochMilli()); }
	 */
    
}
