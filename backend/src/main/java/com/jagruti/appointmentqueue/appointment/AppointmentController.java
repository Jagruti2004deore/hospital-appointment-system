package com.jagruti.appointmentqueue.appointment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // 👤 PATIENT — Book appointment
    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(
            @RequestBody AppointmentRequest request,
            Authentication authentication
    ) {
        appointmentService.bookAppointment(request, authentication);
        return ResponseEntity.ok("Appointment booked");
    }

    // 👤 PATIENT — My dashboard
    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> myAppointments(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                appointmentService.getMyAppointments(authentication)
        );
    }

    // 👤 PATIENT — Cancel appointment
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(
            @PathVariable Long id,
            Authentication authentication
    ) {
        appointmentService.cancelAppointment(id, authentication);
        return ResponseEntity.ok("Appointment cancelled");
    }
}
