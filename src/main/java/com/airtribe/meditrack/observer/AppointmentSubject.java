package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class managing observer registrations and notifications for appointment events.
 */
public class AppointmentSubject {

    private final List<AppointmentObserver> observers = new ArrayList<>();

    public void attach(AppointmentObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(AppointmentObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Appointment appointment, String message) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentEvent(appointment, message);
        }
    }
}
