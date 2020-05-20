package com.saini.rclone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saini.rclone.model.Comment;
import com.saini.rclone.model.Post;
import com.saini.rclone.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
	List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);

}