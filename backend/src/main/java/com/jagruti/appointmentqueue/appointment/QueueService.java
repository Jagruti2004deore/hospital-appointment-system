package com.jagruti.appointmentqueue.appointment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QueueService {

    private final AppointmentRepository appointmentRepository;

    public QueueService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // ✅ Assign next queue number (BOOK)
    public int assignQueueNumber(LocalDate date) {
        return appointmentRepository
                .findMaxQueueNumberForDate(date)
                .map(max -> max + 1)
                .orElse(1);
    }

    // 🔄 Reorder queue after CANCEL
    public void reorderQueueAfterCancel(LocalDate date, int cancelledQueueNumber) {

        List<Appointment> appointments =
                appointmentRepository
                        .findByAppointmentDateAndQueueNumberGreaterThanAndStatus(
                                date,
                                cancelledQueueNumber,
                                AppointmentStatus.WAITING
                        );

        for (Appointment appointment : appointments) {
            appointment.setQueueNumber(appointment.getQueueNumber() - 1);
        }

        appointmentRepository.saveAll(appointments);
    }

    // 🔄 Reorder queue after DONE
    public void reorderQueueAfterDone(LocalDate date, int doneQueueNumber) {

        List<Appointment> appointments =
                appointmentRepository
                        .findByAppointmentDateAndQueueNumberGreaterThanAndStatus(
                                date,
                                doneQueueNumber,
                                AppointmentStatus.WAITING
                        );

        for (Appointment appointment : appointments) {
            appointment.setQueueNumber(appointment.getQueueNumber() - 1);
        }

        appointmentRepository.saveAll(appointments);
    }
}
