package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.CommentDTO;
import com.norsys.knowvault.model.Comment;
import com.norsys.knowvault.model.Page;
import com.norsys.knowvault.repository.CommentRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.service.Impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PageRepository pageRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtAuthenticationToken jwtAuthenticationToken;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private CommentDTO testCommentDTO;
    private Page testPage;

    @BeforeEach
    void setUp() {
        testPage = Page.builder()
                .id(1L)
                .pageNumber(1)
                .content("Test page content")
                .build();

        testComment = Comment.builder()
                .id(1L)
                .text("Test comment")
                .utilisateurLogin("testuser")
                .createdAt(LocalDateTime.now())
                .page(testPage)
                .build();

        testCommentDTO = new CommentDTO();
        testCommentDTO.setId(1L);
        testCommentDTO.setText("Test comment");
        testCommentDTO.setUtilisateurLogin("testuser");
        testCommentDTO.setPageId(1L);
    }

    @Test
    void create_ShouldCreateCommentSuccessfully() {
        // Given
        when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
        when(jwt.getClaim("preferred_username")).thenReturn("testuser");
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);

        // When
        CommentDTO result = commentService.create(testCommentDTO, jwtAuth);

        // Then
        assertNotNull(result);
        assertEquals("Test comment", result.getText());
        assertEquals("testuser", result.getUtilisateurLogin());
        assertEquals(1L, result.getPageId());
        verify(pageRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void create_ShouldThrowException_WhenPageNotFound() {
        // Given
        when(pageRepository.findById(1L)).thenReturn(Optional.empty());

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.create(testCommentDTO, jwtAuth));
        assertEquals("Page introuvable", exception.getMessage());
        verify(pageRepository).findById(1L);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void findAll_ShouldReturnPageOfComments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> comments = List.of(testComment);
        org.springframework.data.domain.Page<Comment> commentPage = new PageImpl<>(comments, pageable, 1);
        when(commentRepository.findAll(pageable)).thenReturn(commentPage);

        // When
        org.springframework.data.domain.Page<CommentDTO> result = commentService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test comment", result.getContent().get(0).getText());
        verify(commentRepository).findAll(pageable);
    }

    @Test
    void searchByContent_ShouldReturnMatchingComments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> comments = List.of(testComment);
        org.springframework.data.domain.Page<Comment> commentPage = new PageImpl<>(comments, pageable, 1);
        when(commentRepository.findByTextContainingIgnoreCase("test", pageable)).thenReturn(commentPage);

        // When
        org.springframework.data.domain.Page<CommentDTO> result = commentService.searchByContent("test", pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test comment", result.getContent().get(0).getText());
        verify(commentRepository).findByTextContainingIgnoreCase("test", pageable);
    }

    @Test
    void findById_ShouldReturnComment_WhenCommentExists() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // When
        CommentDTO result = commentService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test comment", result.getText());
        verify(commentRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenCommentNotFound() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.findById(1L));
        assertEquals("Commentaire introuvable", exception.getMessage());
        verify(commentRepository).findById(1L);
    }

    @Test
    void update_ShouldUpdateCommentTextSuccessfully() {
        // Given
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setText("Updated comment");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        CommentDTO result = commentService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(commentRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void update_ShouldUpdateCommentPageSuccessfully() {
        // Given
        Page newPage = Page.builder().id(2L).pageNumber(2).build();
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setText("Updated comment");
        updateDTO.setPageId(2L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(pageRepository.findById(2L)).thenReturn(Optional.of(newPage));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        CommentDTO result = commentService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        verify(commentRepository).findById(1L);
        verify(pageRepository).findById(2L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void update_ShouldThrowException_WhenCommentNotFound() {
        // Given
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setText("Updated comment");
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.update(1L, updateDTO));
        assertEquals("Commentaire introuvable", exception.getMessage());
        verify(commentRepository).findById(1L);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void update_ShouldThrowException_WhenPageNotFound() {
        // Given
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setText("Updated comment");
        updateDTO.setPageId(999L);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(pageRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.update(1L, updateDTO));
        assertEquals("Page introuvable", exception.getMessage());
        verify(commentRepository).findById(1L);
        verify(pageRepository).findById(999L);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void delete_ShouldDeleteCommentSuccessfully() {
        // Given
        when(commentRepository.existsById(1L)).thenReturn(true);

        // When
        commentService.delete(1L);

        // Then
        verify(commentRepository).existsById(1L);
        verify(commentRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenCommentNotFound() {
        // Given
        when(commentRepository.existsById(1L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> commentService.delete(1L));
        assertEquals("Commentaire introuvable", exception.getMessage());
        verify(commentRepository).existsById(1L);
        verify(commentRepository, never()).deleteById(anyLong());
    }
}
