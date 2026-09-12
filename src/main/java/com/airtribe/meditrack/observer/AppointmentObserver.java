package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;

/**
 * Observer interface for listening to appointment status updates and booking events.
 */
public interface AppointmentObserver {

    /**
     * Called when an appointment event occurs.
     *
     * @param appointment Affected appointment
     * @param eventMessage Description of event
     */
    void onAppointmentEvent(Appointment appointment, String eventMessage);
}
