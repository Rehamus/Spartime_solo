package com.sparta.spartime.web.controller;

import com.sparta.spartime.dto.response.CommentResponseDto;
import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.filter.TestMockFilter;
import com.sparta.spartime.security.config.SecurityConfig;
import com.sparta.spartime.security.principal.UserPrincipal;
import com.sparta.spartime.service.CommentService;
import com.sparta.spartime.service.PostService;
import com.sparta.spartime.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserPrincipal userPrincipal;

    @MockBean
    private CommentService commentService;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new TestMockFilter())
                .build();
    }

    @Test
    void getLikeCommentsPage() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("password")
                .role(User.Role.USER)
                .status(User.Status.ACTIVITY)
                .build();


        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(1L)
                .contents("Test Contents")
                .email("test2@gmail.com")
                .likes(77L)
                .build();

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Page<CommentResponseDto> pageResponse = new PageImpl<>(Collections.singletonList(commentResponseDto));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userPrincipal, null));
        SecurityContextHolder.setContext(securityContext);

        // when
        when(commentService.getLikePage(anyLong(), anyInt(), anyInt(), anyInt()))
                .thenReturn(pageResponse);
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/liked/comments")
                                .param("page", "1")
                                .param("size", "5")
                                .param("asc", "0")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("test2@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contents").value("Test Contents"))
                .andDo(print());

    }

    @Test
    void getLikePostPage() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("password")
                .role(User.Role.USER)
                .status(User.Status.ACTIVITY)
                .build();


        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Contents")
                .build();

        UserPrincipal userPrincipal = new UserPrincipal(user);

        Page<PostResponseDto> pageResponse = new PageImpl<>(Collections.singletonList(postResponseDto));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userPrincipal, null));
        SecurityContextHolder.setContext(securityContext);
        // when
        when(postService.getLikePage(anyLong(), anyInt(), anyInt(), anyInt()))
                .thenReturn(pageResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/liked/posts")
                                .param("page", "1")
                                .param("size", "5")
                                .param("asc", "0")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("Test Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content").value("Test Contents"))
                .andDo(print());

    }
}
