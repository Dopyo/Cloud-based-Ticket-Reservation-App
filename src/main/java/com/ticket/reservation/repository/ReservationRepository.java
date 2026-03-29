package com.ticket.reservation.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ticket.reservation.model.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByCustomerId(String customerId);
}
