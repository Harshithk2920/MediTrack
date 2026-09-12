package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.observer.AppointmentSubject;
import com.airtribe.meditrack.observer.ConsoleNotificationObserver;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service managing Appointment creation, status management, observer notification, and background tasks.
 * Demonstrates Concurrency, Exception Handling, Observer Pattern, and Bill Creation via Factory.
 */
public class AppointmentService {

    private final DataStore<Appointment> appointmentStore;
    private final Map<String, Bill> billMap = new ConcurrentHashMap<>();
    private final AppointmentSubject notificationSubject = new AppointmentSubject();
    private Timer reminderTimer;

    public AppointmentService(DataStore<Appointment> appointmentStore) {
        this.appointmentStore = appointmentStore;
        // Attach default console observer for notifications
        this.notificationSubject.attach(new ConsoleNotificationObserver());
        startBackgroundReminderTask();
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime dateTime, String notes) {
        String id = IdGenerator.getInstance().generateAppointmentId();
        Appointment appointment = new Appointment(id, patient, doctor, dateTime, notes);
        appointmentStore.add(appointment);

        // Notify Observers
        notificationSubject.notifyObservers(appointment, "BOOKED");
        return appointment;
    }

    public Appointment getAppointmentById(String appointmentId) throws AppointmentNotFoundException {
        return appointmentStore.getById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with ID '" + appointmentId + "' not found."));
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public void cancelAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment apt = getAppointmentById(appointmentId);
        apt.setStatus(AppointmentStatus.CANCELLED);

        // Notify Observers
        notificationSubject.notifyObservers(apt, "CANCELLED");
    }

    public void completeAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment apt = getAppointmentById(appointmentId);
        apt.setStatus(AppointmentStatus.COMPLETED);

        // Notify Observers
        notificationSubject.notifyObservers(apt, "COMPLETED");
    }

    /**
     * Generate Bill using BillFactory and Store it.
     */
    public Bill generateBillForAppointment(String appointmentId, BillFactory.BillType billType) throws AppointmentNotFoundException {
        Appointment apt = getAppointmentById(appointmentId);
        Bill bill = BillFactory.createBill(apt, billType);
        billMap.put(bill.getBillId(), bill);
        return bill;
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(billMap.values());
    }

    public Optional<Bill> getBillById(String billId) {
        return Optional.ofNullable(billMap.get(billId));
    }

    public AppointmentSubject getNotificationSubject() {
        return notificationSubject;
    }

    /**
     * Concurrency Feature: Start a periodic background timer task to process upcoming appointment reminders.
     */
    private void startBackgroundReminderTask() {
        this.reminderTimer = new Timer("AppointmentReminderThread", true); // Daemon thread
        this.reminderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Appointment> all = appointmentStore.getAll();
                    LocalDateTime now = LocalDateTime.now();
                    for (Appointment apt : all) {
                        if (apt.getStatus() == AppointmentStatus.CONFIRMED &&
                                apt.getAppointmentDateTime().isBefore(now.plusHours(24)) &&
                                apt.getAppointmentDateTime().isAfter(now)) {
                            // System periodic check - notification engine silently logs upcoming
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[TimerTask Error] " + e.getMessage());
                }
            }
        }, 5000, 60000); // Initial delay 5s, period 60s
    }

    public void shutdown() {
        if (reminderTimer != null) {
            reminderTimer.cancel();
        }
    }
}
