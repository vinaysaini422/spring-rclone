package com.saini.rclone.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.saini.rclone.dto.SubredditDto;
import com.saini.rclone.model.Post;
import com.saini.rclone.model.Subreddit;
import com.saini.rclone.model.User;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
	SubredditDto mapSubredditToDto(Subreddit subreddit);

	default Integer mapPosts(List<Post> numberOfPosts) {
		return numberOfPosts.size();
	}

	@InheritInverseConfiguration
	@Mapping(target = "posts", ignore = true)
	Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User user);

}
