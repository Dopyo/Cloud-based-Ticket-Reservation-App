package com.ticket.reservation.repository;

import com.ticket.reservation.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findByReservationId(String reservationId);
}
