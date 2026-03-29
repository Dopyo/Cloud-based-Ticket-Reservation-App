package com.ticket.reservation.controller;

import com.ticket.reservation.dto.CancelReservationRequestDTO;
import com.ticket.reservation.dto.CreateReservationRequestDTO;
import com.ticket.reservation.model.Reservation;
import com.ticket.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> createReservation(@RequestBody CreateReservationRequestDTO request) {
        Reservation reservation = reservationService.createReservation(
                request.getCustomerId(),
                request.getEventId()
        );

        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Reservation> cancelReservation(@RequestBody CancelReservationRequestDTO request) {
        Reservation reservation = reservationService.cancelReservation(
                request.getCustomerId(),
                request.getReservationId()
        );

        return ResponseEntity.ok(reservation);
    }
}
