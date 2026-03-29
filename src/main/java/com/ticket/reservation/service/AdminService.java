package com.ticket.reservation.service;

import com.ticket.reservation.dto.AdminEventRequestDTO;
import com.ticket.reservation.exception.AuthenticationException;
import com.ticket.reservation.model.Administrator;
import com.ticket.reservation.model.Event;
import com.ticket.reservation.model.User;
import com.ticket.reservation.repository.EventRepository;
import com.ticket.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public AdminService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Event addEvent(String authenticatedEmail, AdminEventRequestDTO request) {
        validateAdmin(authenticatedEmail);

        Event event = new Event();
        event.setName(request.getName());
        event.setCategory(request.getCategory());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setDateTime(request.getDateTime());
        event.setPrice(request.getPrice());
        event.setCancelled(false);

        return eventRepository.save(event);
    }

    public Event editEvent(String authenticatedEmail, String eventId, AdminEventRequestDTO request) {
        validateAdmin(authenticatedEmail);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.setName(request.getName());
        event.setCategory(request.getCategory());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setDateTime(request.getDateTime());
        event.setPrice(request.getPrice());

        return eventRepository.save(event);
    }

    public Event cancelEvent(String authenticatedEmail, String eventId) {
        validateAdmin(authenticatedEmail);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        event.setCancelled(true);
        return eventRepository.save(event);
    }

    private void validateAdmin(String authenticatedEmail) {
        if (authenticatedEmail == null || authenticatedEmail.isBlank()) {
            throw new AuthenticationException("User not authenticated");
        }

        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (!(user instanceof Administrator)) {
            throw new IllegalArgumentException("User is not an administrator");
        }
    }
}