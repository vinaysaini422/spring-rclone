package com.saini.rclone.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.saini.rclone.dto.PostRequest;
import com.saini.rclone.dto.PostResponse;
import com.saini.rclone.exceptions.PostNotFoundException;
import com.saini.rclone.exceptions.SubredditNotFoundException;
import com.saini.rclone.exceptions.UsernameNotFoundexception;
import com.saini.rclone.mapper.PostMapper;
import com.saini.rclone.model.Post;
import com.saini.rclone.model.Subreddit;
import com.saini.rclone.model.User;
import com.saini.rclone.repositories.PostRepository;
import com.saini.rclone.repositories.SubredditRepository;
import com.saini.rclone.repositories.UserRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        Post post =postMapper.map(postRequest, subreddit, authService.getCurrentUser());
        postRepository.save(post);
    }

    @Transactional
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new PostNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());	
    }

    @Transactional
    public List<PostResponse> getPostsByUsername(String username) {
    	Optional<User> optional =userRepository.findByUsername(username);
    	User user =null;
    	if(optional.isPresent()) {
    		user = optional.get();
    	}else {
    		throw new UsernameNotFoundexception("Username not found");
    	}
        
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
    
}
