package com.jagruti.appointmentqueue.appointment;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔑 Logged-in user (patient)
    @Column(nullable = false)
    private String userEmail;

    // 👤 Patient details
    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String contactNumber;

    // 🆕 NEW / FOLLOWUP
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType appointmentType;

    // 📅 Appointment info
    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private int queueNumber;

    // ⏳ WAITING / DONE / CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    // 🩺 Doctor response
    private String doctorMessage;

    // ---------- getters & setters ----------

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getDoctorMessage() {
        return doctorMessage;
    }

    public void setDoctorMessage(String doctorMessage) {
        this.doctorMessage = doctorMessage;
    }
}
