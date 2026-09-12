package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.*;
import java.io.*;
import java.util.*;

/**
 * CSV File Persistence Utility class using try-with-resources and String.split(",").
 * Demonstrates File I/O, CSV parsing, Exception Handling, and Data Conversion.
 */
public final class CSVUtil {

    private CSVUtil() {
        // Utility Class
    }

    private static void ensureParentDirectory(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    // --- PATIENT CSV OPERATIONS ---

    public static void savePatientsToCSV(List<Patient> patients, String filePath) throws IOException {
        ensureParentDirectory(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,age,gender,contactNumber,email,bloodGroup,medicalHistory\n");
            for (Patient p : patients) {
                String historyStr = String.join(";", p.getMedicalHistory());
                String line = String.format("%s,%s,%d,%s,%s,%s,%s,%s\n",
                        p.getId(), escapeCsv(p.getName()), p.getAge(), p.getGender(),
                        p.getContactNumber(), p.getEmail(), p.getBloodGroup(), escapeCsv(historyStr));
                writer.write(line);
            }
        }
    }

    public static List<Patient> loadPatientsFromCSV(String filePath) throws IOException {
        List<Patient> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",", -1);
                if (tokens.length >= 8) {
                    String id = tokens[0].trim();
                    String name = tokens[1].trim();
                    int age = Integer.parseInt(tokens[2].trim());
                    String gender = tokens[3].trim();
                    String contact = tokens[4].trim();
                    String email = tokens[5].trim();
                    String blood = tokens[6].trim();
                    String historyRaw = tokens[7].trim();

                    List<String> history = new ArrayList<>();
                    if (!historyRaw.isEmpty()) {
                        String[] hArray = historyRaw.split(";");
                        for (String h : hArray) {
                            if (!h.trim().isEmpty()) history.add(h.trim());
                        }
                    }
                    list.add(new Patient(id, name, age, gender, contact, email, blood, history));
                }
            }
        }
        return list;
    }

    // --- DOCTOR CSV OPERATIONS ---

    public static void saveDoctorsToCSV(List<Doctor> doctors, String filePath) throws IOException {
        ensureParentDirectory(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,name,age,gender,contactNumber,email,specialization,consultationFee,available\n");
            for (Doctor d : doctors) {
                String line = String.format("%s,%s,%d,%s,%s,%s,%s,%.2f,%b\n",
                        d.getId(), escapeCsv(d.getName()), d.getAge(), d.getGender(),
                        d.getContactNumber(), d.getEmail(), d.getSpecialization().name(),
                        d.getConsultationFee(), d.isAvailable());
                writer.write(line);
            }
        }
    }

    public static List<Doctor> loadDoctorsFromCSV(String filePath) throws IOException {
        List<Doctor> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",", -1);
                if (tokens.length >= 9) {
                    String id = tokens[0].trim();
                    String name = tokens[1].trim();
                    int age = Integer.parseInt(tokens[2].trim());
                    String gender = tokens[3].trim();
                    String contact = tokens[4].trim();
                    String email = tokens[5].trim();
                    Specialization spec = Specialization.fromString(tokens[6].trim());
                    double fee = Double.parseDouble(tokens[7].trim());
                    boolean avail = Boolean.parseBoolean(tokens[8].trim());

                    list.add(new Doctor(id, name, age, gender, contact, email, spec, fee, avail));
                }
            }
        }
        return list;
    }

    // --- APPOINTMENT CSV OPERATIONS ---

    public static void saveAppointmentsToCSV(List<Appointment> appointments, String filePath) throws IOException {
        ensureParentDirectory(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("appointmentId,patientId,doctorId,dateTime,status,notes\n");
            for (Appointment a : appointments) {
                String line = String.format("%s,%s,%s,%s,%s,%s\n",
                        a.getAppointmentId(), a.getPatient().getId(), a.getDoctor().getId(),
                        DateUtil.format(a.getAppointmentDateTime()), a.getStatus().name(),
                        escapeCsv(a.getNotes()));
                writer.write(line);
            }
        }
    }

    public static List<Map<String, String>> loadAppointmentRecordsFromCSV(String filePath) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",", -1);
                if (tokens.length >= 6) {
                    Map<String, String> rec = new HashMap<>();
                    rec.put("appointmentId", tokens[0].trim());
                    rec.put("patientId", tokens[1].trim());
                    rec.put("doctorId", tokens[2].trim());
                    rec.put("dateTime", tokens[3].trim());
                    rec.put("status", tokens[4].trim());
                    rec.put("notes", tokens[5].trim());
                    list.add(rec);
                }
            }
        }
        return list;
    }

    private static String escapeCsv(String input) {
        if (input == null) return "";
        return input.replace(",", ";");
    }
}
