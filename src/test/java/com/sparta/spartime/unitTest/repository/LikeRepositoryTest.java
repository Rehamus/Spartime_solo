package com.sparta.spartime.unitTest.repository;

import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.entity.Comment;
import com.sparta.spartime.entity.Like;
import com.sparta.spartime.entity.Post;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.repository.CommentRepository;
import com.sparta.spartime.repository.LikeRepository.LikeRepository;
import com.sparta.spartime.repository.PostRepository;
import com.sparta.spartime.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class LikeRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;



    @Test
    void testGetPostsLikedByUser() {
    }

    @Test
    void getCommentsLikedByUser() {
    }
}