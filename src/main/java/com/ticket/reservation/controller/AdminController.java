package com.ticket.reservation.controller;

import com.ticket.reservation.dto.AdminEventRequestDTO;
import com.ticket.reservation.dto.LoginRequestDTO;
import com.ticket.reservation.dto.LoginResponseDTO;
import com.ticket.reservation.dto.UserRegistrationRequestDTO;
import com.ticket.reservation.dto.UserRegistrationResponseDTO;
import com.ticket.reservation.model.Administrator;
import com.ticket.reservation.model.Event;
import com.ticket.reservation.model.User;
import com.ticket.reservation.service.AdminService;
import com.ticket.reservation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new administrator")
    public ResponseEntity<UserRegistrationResponseDTO> registerAdmin(
            @Valid @RequestBody UserRegistrationRequestDTO request
    ) {
        User admin = new Administrator(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getPassword()
        );

        User savedAdmin = userService.createUser(admin);
        UserRegistrationResponseDTO responseDTO = new UserRegistrationResponseDTO(savedAdmin);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "Login as administrator with email/phone and password")
    public ResponseEntity<LoginResponseDTO> loginAdmin(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        User user = userService.login(request.getIdentifier(), request.getPassword());

        if (!(user instanceof Administrator)) {
            throw new IllegalArgumentException("User is not an administrator");
        }

        String token = userService.generateToken(user);
        LoginResponseDTO responseDTO = new LoginResponseDTO(user, token);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/events")
    public ResponseEntity<Event> addEvent(
            @AuthenticationPrincipal String email,
            @RequestBody AdminEventRequestDTO request
    ) {
        return ResponseEntity.ok(adminService.addEvent(email, request));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Event> editEvent(
            @AuthenticationPrincipal String email,
            @PathVariable String eventId,
            @RequestBody AdminEventRequestDTO request
    ) {
        return ResponseEntity.ok(adminService.editEvent(email, eventId, request));
    }

    @PatchMapping("/events/{eventId}/cancel")
    public ResponseEntity<Event> cancelEvent(
            @AuthenticationPrincipal String email,
            @PathVariable String eventId
    ) {
        return ResponseEntity.ok(adminService.cancelEvent(email, eventId));
    }
}