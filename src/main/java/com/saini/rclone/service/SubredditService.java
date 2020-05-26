package com.saini.rclone.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.saini.rclone.dto.SubredditDto;
import com.saini.rclone.exceptions.SubredditNotFoundException;
import com.saini.rclone.mapper.SubredditMapper;
import com.saini.rclone.model.Subreddit;
import com.saini.rclone.repositories.SubredditRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	private final AuthService authService;
    
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        //Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
    	Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto, authService.getCurrentUser()));
        subredditDto.setId(save.getId());
        return subredditDto;
    }
    
    @Transactional
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }
    
    public SubredditDto getSubreddit(Long id) {
    	Subreddit dto =subredditRepository.findById(id).orElseThrow(() -> new SubredditNotFoundException("Subreddit does not exist"));
    	return subredditMapper.mapSubredditToDto(dto);
    	
    }
    
	/*
	 * private SubredditDto mapToDto(Subreddit subreddit) { return
	 * SubredditDto.builder() .name(subreddit.getName()) .id(subreddit.getId())
	 * .numberOfPosts(subreddit.getPosts().size()) .build(); }
	 * 
	 * private Subreddit mapDtoToSubreddit(SubredditDto subredditDto) { return
	 * Subreddit.builder().name(subredditDto.getName())
	 * .description(subredditDto.getDescription()) .build(); }
	 */
}
