package com.airtribe.meditrack.constants;

import java.io.File;

/**
 * Application-wide constants for MediTrack.
 * Demonstrates static fields, static initialization blocks, and access modifiers.
 */
public final class Constants {

    // Default Tax Rate (18% GST/Tax)
    public static final double TAX_RATE = 0.18;
    
    // Default Currency Symbol
    public static final String CURRENCY_SYMBOL = "$";

    // File Storage Paths
    public static final String DATA_DIR = "data";
    public static final String PATIENTS_CSV = DATA_DIR + File.separator + "patients.csv";
    public static final String DOCTORS_CSV = DATA_DIR + File.separator + "doctors.csv";
    public static final String APPOINTMENTS_CSV = DATA_DIR + File.separator + "appointments.csv";
    public static final String DATASTORE_SER = DATA_DIR + File.separator + "datastore.ser";

    // Application Metadata
    public static final String APP_NAME = "MediTrack — Clinic & Appointment Management System";
    public static final String APP_VERSION = "1.0.0";

    // Static Initialization Block to ensure data directory exists
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("[CONFIG] Initialized storage directory: " + dir.getAbsolutePath());
            }
        }
    }

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated.");
    }
}
