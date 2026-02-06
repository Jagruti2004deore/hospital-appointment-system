package com.jagruti.appointmentqueue.appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // =========================
    // 👤 Patient dashboard
    // =========================
    List<Appointment> findByUserEmail(String userEmail);

    // =========================
    // 🔥 Queue logic (USP)
    // =========================
    @Query("""
        SELECT MAX(a.queueNumber)
        FROM Appointment a
        WHERE a.appointmentDate = :date
          AND a.status = 'WAITING'
    """)
    Optional<Integer> findMaxQueueNumberForDate(@Param("date") LocalDate date);

    List<Appointment> findByAppointmentDateAndQueueNumberGreaterThanAndStatus(
            LocalDate date,
            int queueNumber,
            AppointmentStatus status
    );

    // =========================
    // 👨‍⚕️ Doctor dashboard
    // =========================
    List<Appointment> findByAppointmentTypeAndStatus(
            AppointmentType appointmentType,
            AppointmentStatus status
    );

    List<Appointment> findByStatusOrderByQueueNumberAsc(
            AppointmentStatus status
    );


    List<Appointment> findByAppointmentType(
        AppointmentType appointmentType
    );

    

}
