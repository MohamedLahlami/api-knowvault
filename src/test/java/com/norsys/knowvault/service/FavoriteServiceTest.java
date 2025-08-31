package com.norsys.knowvault.service;

import com.norsys.knowvault.dto.FavoriteDTO;
import com.norsys.knowvault.exception.FavoriteNotFoundException;
import com.norsys.knowvault.model.*;
import com.norsys.knowvault.repository.FavoriteRepository;
import com.norsys.knowvault.repository.PageRepository;
import com.norsys.knowvault.repository.UtilisateurRepository;
import com.norsys.knowvault.service.Impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PageRepository pageRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Favorite testFavorite;
    private Utilisateur testUser;
    private Page testPage;
    private Chapter testChapter;
    private Book testBook;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();

        testUser = Utilisateur.builder()
                .id(testUserId)
                .login("testuser")
                .firstName("John")
                .lastName("Doe")
                .build();

        testBook = Book.builder()
                .id(1L)
                .bookTitle("Test Book")
                .build();

        testChapter = Chapter.builder()
                .id(1L)
                .chapterTitle("Test Chapter")
                .book(testBook)
                .build();

        testPage = Page.builder()
                .id(1L)
                .pageNumber(1)
                .content("Test page content")
                .chapter(testChapter)
                .build();

        testFavorite = Favorite.builder()
                .id(1L)
                .user(testUser)
                .page(testPage)
                .build();
    }

    @Test
    void deleteFavoriteById_ShouldDeleteFavoriteSuccessfully() {
        // Given
        when(favoriteRepository.existsById(1L)).thenReturn(true);

        // When
        favoriteService.deleteFavoriteById(1L);

        // Then
        verify(favoriteRepository).existsById(1L);
        verify(favoriteRepository).deleteById(1L);
    }

    @Test
    void deleteFavoriteById_ShouldThrowException_WhenFavoriteNotFound() {
        // Given
        when(favoriteRepository.existsById(1L)).thenReturn(false);

        // When & Then
        FavoriteNotFoundException exception = assertThrows(FavoriteNotFoundException.class,
                () -> favoriteService.deleteFavoriteById(1L));
        assertEquals("Favori non trouvé avec id: 1", exception.getMessage());
        verify(favoriteRepository).existsById(1L);
        verify(favoriteRepository, never()).deleteById(anyLong());
    }

    @Test
    void getFavoritesByUser_ShouldReturnUserFavorites() {
        // Given
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaimAsString("sub")).thenReturn(testUserId.toString());
            when(utilisateurRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
            when(favoriteRepository.findByUser(testUser)).thenReturn(List.of(testFavorite));

            // When
            List<FavoriteDTO> result = favoriteService.getFavoritesByUser();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
            assertEquals(testUserId, result.get(0).getUserId());
            assertEquals(1L, result.get(0).getPageId());
            verify(favoriteRepository).findByUser(testUser);
        }
    }

    @Test
    void getFavoritesByUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaimAsString("sub")).thenReturn(testUserId.toString());
            when(utilisateurRepository.findById(testUserId)).thenReturn(Optional.empty());

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> favoriteService.getFavoritesByUser());
            assertEquals("Utilisateur introuvable", exception.getMessage());
            verify(utilisateurRepository).findById(testUserId);
        }
    }

    @Test
    void toggleFavorite_ShouldRemoveFavorite_WhenFavoriteExists() {
        // Given
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaimAsString("sub")).thenReturn(testUserId.toString());
            when(utilisateurRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
            when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
            when(favoriteRepository.findByUserAndPage(testUser, testPage)).thenReturn(Optional.of(testFavorite));

            // When
            FavoriteDTO result = favoriteService.toggleFavorite(1L);

            // Then
            assertNull(result);
            verify(favoriteRepository).findByUserAndPage(testUser, testPage);
            verify(favoriteRepository).delete(testFavorite);
            verify(favoriteRepository, never()).save(any(Favorite.class));
        }
    }

    @Test
    void toggleFavorite_ShouldCreateFavorite_WhenFavoriteDoesNotExist() {
        // Given
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaimAsString("sub")).thenReturn(testUserId.toString());
            when(utilisateurRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
            when(pageRepository.findById(1L)).thenReturn(Optional.of(testPage));
            when(favoriteRepository.findByUserAndPage(testUser, testPage)).thenReturn(Optional.empty());
            when(favoriteRepository.save(any(Favorite.class))).thenReturn(testFavorite);

            // When
            FavoriteDTO result = favoriteService.toggleFavorite(1L);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(testUserId, result.getUserId());
            assertEquals(1L, result.getPageId());
            assertEquals(1, result.getPageNumber());
            assertEquals("Test Chapter", result.getChapterTitle());
            assertEquals("Test Book", result.getBookTitle());
            verify(favoriteRepository).findByUserAndPage(testUser, testPage);
            verify(favoriteRepository).save(any(Favorite.class));
        }
    }

    @Test
    void toggleFavorite_ShouldThrowException_WhenPageNotFound() {
        // Given
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaimAsString("sub")).thenReturn(testUserId.toString());
            when(utilisateurRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
            when(pageRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> favoriteService.toggleFavorite(1L));
            assertEquals("Page introuvable", exception.getMessage());
            verify(pageRepository).findById(1L);
            verify(favoriteRepository, never()).save(any(Favorite.class));
        }
    }

    @Test
    void OnlyFavoritesForUser_ShouldReturnDetailedFavorites() {
        // Given
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(jwt.getClaimAsString("sub")).thenReturn(testUserId.toString());
            when(utilisateurRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
            when(favoriteRepository.findByUser(testUser)).thenReturn(List.of(testFavorite));

            // When
            List<FavoriteDTO> result = favoriteService.OnlyFavoritesForUser();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            FavoriteDTO dto = result.get(0);
            assertEquals(1L, dto.getId());
            assertEquals(testUserId, dto.getUserId());
            assertEquals(1L, dto.getPageId());
            assertEquals(1, dto.getPageNumber());
            assertEquals("Test Chapter", dto.getChapterTitle());
            assertEquals("Test Book", dto.getBookTitle());
            verify(favoriteRepository).findByUser(testUser);
        }
    }
}
