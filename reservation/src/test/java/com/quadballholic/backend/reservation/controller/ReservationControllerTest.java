package com.quadballholic.backend.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadballholic.backend.reservation.entity.ReservationEntity;
import com.quadballholic.backend.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@Import(ReservationControllerTest.TestSecurityConfig.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- GET ALL ---
    @Test
    @WithMockUser(roles = "ORGANIZATION_MANAGER")
    void getAllReservations_ShouldReturnList() throws Exception {
        ReservationEntity r1 = new ReservationEntity(); r1.setId(1L);
        when(reservationService.findAllReservations()).thenReturn(Arrays.asList(r1));

        mockMvc.perform(get("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "SPECTATOR")
    void getAllReservations_ShouldReturnForbidden_ForSpectator() throws Exception {
        mockMvc.perform(get("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // --- GET BY ID ---
    @Test
    @WithMockUser(roles = "SPECTATOR")
    void getReservationById_ShouldReturnOk_WhenFound() throws Exception {
        Long id = 1L;
        ReservationEntity r = new ReservationEntity(); r.setId(id);
        when(reservationService.findReservationById(id)).thenReturn(r);

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "SPECTATOR")
    void getReservationById_ShouldReturnNotFound_WhenMissing() throws Exception {
        Long id = 999L;
        when(reservationService.findReservationById(id))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/reservations/{id}", id))
                .andExpect(status().isNotFound());
    }

    // --- CREATE ---
    @Test
    @WithMockUser(roles = "SPECTATOR")
    void createReservation_ShouldReturnCreated() throws Exception {
        ReservationEntity input = new ReservationEntity();
        input.setMatchId(1L);

        ReservationEntity output = new ReservationEntity();
        output.setId(100L);

        when(reservationService.createReservation(any())).thenReturn(output);

        mockMvc.perform(post("/api/reservations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    @WithMockUser(roles = "SPECTATOR")
    void createReservation_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        ReservationEntity invalid = new ReservationEntity();

        when(reservationService.createReservation(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/api/reservations")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    // --- UPDATE ---
    @Test
    @WithMockUser(roles = "ORGANIZATION_MANAGER")
    void updateReservation_ShouldReturnOk() throws Exception {
        Long id = 1L;
        ReservationEntity update = new ReservationEntity();

        when(reservationService.updateReservation(eq(id), any())).thenReturn(update);

        mockMvc.perform(put("/api/reservations/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());
    }

    // --- DELETE ---
    @Test
    @WithMockUser(roles = "ORGANIZATION_MANAGER")
    void deleteReservation_ShouldReturnOk() throws Exception {
        Long id = 1L;
        // Void method, doNothing by default

        mockMvc.perform(delete("/api/reservations/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
    }
}