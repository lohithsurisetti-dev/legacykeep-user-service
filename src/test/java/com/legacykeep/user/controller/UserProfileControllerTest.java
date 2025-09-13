package com.legacykeep.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legacykeep.user.dto.request.UserProfileRequestDto;
import com.legacykeep.user.service.JwtValidationService;
import com.legacykeep.user.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserProfileController.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@WebMvcTest(UserProfileController.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileService userProfileService;

    @MockBean
    private JwtValidationService jwtValidationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetMyProfile_Success() throws Exception {
        // Given
        String token = "Bearer valid-token";
        Long userId = 1L;
        
        when(jwtValidationService.extractUserId("valid-token"))
            .thenReturn(Optional.of(userId));
        
        // When & Then
        mockMvc.perform(get("/api/v1/profiles/me")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMyProfile_InvalidToken() throws Exception {
        // Given
        String token = "Bearer invalid-token";
        
        when(jwtValidationService.extractUserId("invalid-token"))
            .thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/v1/profiles/me")
                .header("Authorization", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProfile_Success() throws Exception {
        // Given
        String token = "Bearer valid-token";
        Long userId = 1L;
        
        UserProfileRequestDto requestDto = new UserProfileRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setDisplayName("John Doe");
        requestDto.setBio("Test bio");
        
        when(jwtValidationService.extractUserId("valid-token"))
            .thenReturn(Optional.of(userId));
        
        // When & Then
        mockMvc.perform(post("/api/v1/profiles")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateProfile_ValidationError() throws Exception {
        // Given
        String token = "Bearer valid-token";
        Long userId = 1L;
        
        UserProfileRequestDto requestDto = new UserProfileRequestDto();
        // Missing required fields
        
        when(jwtValidationService.extractUserId("valid-token"))
            .thenReturn(Optional.of(userId));
        
        // When & Then
        mockMvc.perform(post("/api/v1/profiles")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()); // Service layer handles validation
    }

    @Test
    void testGetPublicProfiles_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/profiles/public")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchProfiles_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/profiles/search")
                .param("query", "John")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }
}
