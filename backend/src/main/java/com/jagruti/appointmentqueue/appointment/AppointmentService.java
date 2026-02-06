package com.jagruti.appointmentqueue.appointment;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final QueueService queueService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            QueueService queueService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.queueService = queueService;
    }

    // =================================================
    // 👤 PATIENT SERVICES
    // =================================================

    // 📌 Book appointment (PATIENT)
    public Appointment bookAppointment(
            AppointmentRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();

        int queueNumber =
                queueService.assignQueueNumber(request.getAppointmentDate());

        Appointment appointment = new Appointment();

        // 🔐 Logged-in patient
        appointment.setUserEmail(userEmail);

        // 👤 Patient details
        appointment.setPatientName(request.getPatientName());
        appointment.setAge(request.getAge());
        appointment.setBloodGroup(request.getBloodGroup());
        appointment.setGender(request.getGender());
        appointment.setContactNumber(request.getContactNumber());
        appointment.setAppointmentType(request.getAppointmentType());

        // 📅 Appointment info
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setQueueNumber(queueNumber);
        appointment.setStatus(AppointmentStatus.WAITING);

        return appointmentRepository.save(appointment);
    }

    // 📌 Patient dashboard – view own appointments
    public List<Appointment> getMyAppointments(Authentication authentication) {
        return appointmentRepository.findByUserEmail(
                authentication.getName()
        );
    }

    // 📌 Patient cancels appointment
    public void cancelAppointment(Long id, Authentication authentication) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 🔒 Ownership check
        if (!appointment.getUserEmail().equals(authentication.getName())) {
            throw new RuntimeException("Unauthorized cancellation");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        queueService.reorderQueueAfterCancel(
                appointment.getAppointmentDate(),
                appointment.getQueueNumber()
        );
    }

    // =================================================
    // 👨‍⚕️ DOCTOR / ADMIN SERVICES
    // =================================================

    // 📋 Doctor dashboard – all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // 🆕 Doctor dashboard – filter NEW / FOLLOWUP
    public List<Appointment> getByAppointmentType(AppointmentType type) {
        return appointmentRepository.findByAppointmentType(type);
    }

    // 👁️ View full appointment details
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // ✅ Doctor confirms appointment + message
    public void confirmAppointment(Long id, String doctorMessage) {

        Appointment appointment = getAppointmentById(id);

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setDoctorMessage(doctorMessage);

        appointmentRepository.save(appointment);
    }

    // ❌ Doctor cancels appointment
    public void adminCancelAppointment(Long id) {

        Appointment appointment = getAppointmentById(id);

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        queueService.reorderQueueAfterCancel(
                appointment.getAppointmentDate(),
                appointment.getQueueNumber()
        );
    }

    // 🏁 Doctor marks DONE after visit
    public void markDone(Long id) {

        Appointment appointment = getAppointmentById(id);

        appointment.setStatus(AppointmentStatus.DONE);
        appointmentRepository.save(appointment);

        queueService.reorderQueueAfterDone(
                appointment.getAppointmentDate(),
                appointment.getQueueNumber()
        );
    }
}
