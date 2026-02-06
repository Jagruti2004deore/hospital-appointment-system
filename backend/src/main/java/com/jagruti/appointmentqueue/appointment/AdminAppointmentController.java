package com.jagruti.appointmentqueue.appointment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // 📋 1️⃣ All appointments
    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // 🆕 2️⃣ New patients
    @GetMapping("/new")
    public ResponseEntity<List<Appointment>> getNewPatients() {
        return ResponseEntity.ok(
                appointmentService.getByAppointmentType(AppointmentType.NEW)
        );
    }

    // 🔁 3️⃣ Follow-up patients
    @GetMapping("/followup")
    public ResponseEntity<List<Appointment>> getFollowups() {
        return ResponseEntity.ok(
                appointmentService.getByAppointmentType(AppointmentType.FOLLOWUP)
        );
    }

    // 👁️ 4️⃣ View full appointment details
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    // ✅ 5️⃣ Confirm appointment + doctor message
    @PostMapping("/{id}/confirm")
    public ResponseEntity<String> confirmAppointment(
            @PathVariable Long id,
            @RequestBody DoctorMessageRequest request
    ) {
        appointmentService.confirmAppointment(id, request.getDoctorMessage());
        return ResponseEntity.ok("Appointment confirmed");
    }

    // ❌ 6️⃣ Cancel appointment (doctor/admin)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.adminCancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled by doctor");
    }
}
