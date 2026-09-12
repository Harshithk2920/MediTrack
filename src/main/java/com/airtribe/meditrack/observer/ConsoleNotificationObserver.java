package com.airtribe.meditrack.observer;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;

/**
 * Observer implementation that logs appointment reminders and notifications to console.
 */
public class ConsoleNotificationObserver implements AppointmentObserver {

    @Override
    public void onAppointmentEvent(Appointment appointment, String eventMessage) {
        System.out.printf("[NOTIFICATION ENGINE] Event: %s | AptID: %s | Patient: %s | Doctor: Dr. %s | Time: %s | Status: %s%n",
                eventMessage,
                appointment.getAppointmentId(),
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                DateUtil.format(appointment.getAppointmentDateTime()),
                appointment.getStatus());
    }
}
