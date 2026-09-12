package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton ID Generator utilizing AtomicInteger for thread-safe sequence generation.
 * Demonstrates both Eager and Lazy Singleton initialization patterns and static blocks.
 */
public class IdGenerator {

    // Eager Singleton Instance
    private static final IdGenerator EAGER_INSTANCE = new IdGenerator();

    // Lazy Singleton Instance
    private static volatile IdGenerator lazyInstance;

    // Sequences using AtomicInteger for thread safety
    private final AtomicInteger patientSeq;
    private final AtomicInteger doctorSeq;
    private final AtomicInteger appointmentSeq;
    private final AtomicInteger billSeq;

    // Static Initialization Block
    static {
        System.out.println("[IdGenerator] Static initialization block executed. ID sequences ready.");
    }

    // Private Constructor
    private IdGenerator() {
        this.patientSeq = new AtomicInteger(1000);
        this.doctorSeq = new AtomicInteger(5000);
        this.appointmentSeq = new AtomicInteger(8000);
        this.billSeq = new AtomicInteger(9000);
    }

    /**
     * Eager Initialization Singleton Getter.
     *
     * @return Eagerly created IdGenerator instance
     */
    public static IdGenerator getInstance() {
        return EAGER_INSTANCE;
    }

    /**
     * Lazy Initialization Singleton Getter with Double-Checked Locking.
     *
     * @return Lazily created IdGenerator instance
     */
    public static IdGenerator getLazyInstance() {
        if (lazyInstance == null) {
            synchronized (IdGenerator.class) {
                if (lazyInstance == null) {
                    lazyInstance = new IdGenerator();
                }
            }
        }
        return lazyInstance;
    }

    public String generatePatientId() {
        return "PAT-" + patientSeq.incrementAndGet();
    }

    public String generateDoctorId() {
        return "DOC-" + doctorSeq.incrementAndGet();
    }

    public String generateAppointmentId() {
        return "APT-" + appointmentSeq.incrementAndGet();
    }

    public String generateBillId() {
        return "BIL-" + billSeq.incrementAndGet();
    }

    /**
     * Set sequence seed (useful when restoring state from persisted data).
     */
    public synchronized void setSeed(int patientStart, int doctorStart, int appointmentStart, int billStart) {
        if (patientStart > patientSeq.get()) patientSeq.set(patientStart);
        if (doctorStart > doctorSeq.get()) doctorSeq.set(doctorStart);
        if (appointmentStart > appointmentSeq.get()) appointmentSeq.set(appointmentStart);
        if (billStart > billSeq.get()) billSeq.set(billStart);
    }
}
