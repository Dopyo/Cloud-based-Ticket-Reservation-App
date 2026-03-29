package com.ticket.reservation.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ticket.reservation.model.Reservation;
import com.ticket.reservation.service.ReservationService;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest {

    @Test
    void createReservation_returns200() throws Exception {
        ReservationService service = Mockito.mock(ReservationService.class);
        ReservationController controller = new ReservationController(service);

        Reservation reservation = new Reservation("customer1", LocalDateTime.now(), "ACTIVE");
        Mockito.when(service.createReservation("customer1", "event1"))
                .thenReturn(reservation);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String requestBody = """
                {
                  "customerId": "customer1",
                  "eventId": "event1"
                }
                """;

        mockMvc.perform(post("/api/reservations/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void cancelReservation_returns200() throws Exception {
        ReservationService service = Mockito.mock(ReservationService.class);
        ReservationController controller = new ReservationController(service);

        Reservation reservation = new Reservation("customer1", LocalDateTime.now(), "CANCELLED");
        Mockito.when(service.cancelReservation("customer1", "reservation1"))
                .thenReturn(reservation);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String requestBody = """
                {
                  "customerId": "customer1",
                  "reservationId": "reservation1"
                }
                """;

        mockMvc.perform(post("/api/reservations/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}