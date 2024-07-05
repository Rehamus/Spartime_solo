package com.sparta.spartime.web.controller;

import com.sparta.spartime.dto.response.PostResponseDto;
import com.sparta.spartime.entity.User;
import com.sparta.spartime.filter.TestMockFilter;
import com.sparta.spartime.searchCond.PostSearchCond;
import com.sparta.spartime.security.config.SecurityConfig;
import com.sparta.spartime.security.principal.UserPrincipal;
import com.sparta.spartime.service.CommentService;
import com.sparta.spartime.service.FollowService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(
        controllers = {FollowController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @MockBean
    private FollowService followService;

    @MockBean
    private UserPrincipal userPrincipal;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new TestMockFilter())
                .build();

    }

    @Test
    public void testFollowingPost() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("password")
                .role(User.Role.USER)
                .status(User.Status.ACTIVITY)
                .build();
        UserPrincipal userPrincipal = new UserPrincipal(user);

        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Contents")
                .build();

        Page<PostResponseDto> pageResponse = new PageImpl<>(Collections.singletonList(postResponseDto));

        // Set up SecurityContext with authenticated user
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userPrincipal, null));
        SecurityContextHolder.setContext(securityContext);

        when(followService.followingPost(any(PostSearchCond.class), anyInt(), anyInt(), anyInt()))
                .thenReturn(pageResponse);

        // when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/following/posts")
                                .param("title", "Test Post")
                                .param("contents", "This is a test post")
                                .param("page", "1")
                                .param("size", "5")
                                .param("asc", "0")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("Test Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content").value("Test Contents"))
                .andDo(print());

        // Verify that followService's method was called with correct arguments
        verify(followService, times(1)).followingPost(any(PostSearchCond.class), eq(0), eq(5), eq(0));
    }

}