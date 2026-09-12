package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.strategy.DiscountBilling;
import com.airtribe.meditrack.strategy.InsuranceBilling;
import com.airtribe.meditrack.strategy.StandardBilling;
import com.airtribe.meditrack.util.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Interactive Console CLI Entry point for MediTrack.
 * Handles command-line arguments (--loadData) and menu-driven navigation.
 */
public class Main {

    private final DataStore<Patient> patientStore = new DataStore<>();
    private final DataStore<Doctor> doctorStore = new DataStore<>();
    private final DataStore<Appointment> appointmentStore = new DataStore<>();

    private final PatientService patientService = new PatientService(patientStore);
    private final DoctorService doctorService = new DoctorService(doctorStore);
    private final AppointmentService appointmentService = new AppointmentService(appointmentStore);

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main app = new Main();
        
        // Parse Command-line Arguments (--loadData flag support)
        boolean autoLoad = false;
        for (String arg : args) {
            if ("--loadData".equalsIgnoreCase(arg.trim())) {
                autoLoad = true;
                break;
            }
        }

        if (autoLoad) {
            System.out.println("[CLI ARG] '--loadData' detected. Automatically pre-populating system state...");
            app.loadDataFromCSV();
        } else {
            app.seedSampleData();
        }

        app.runMenuLoop();
    }

    private void seedSampleData() {
        System.out.println("[SYSTEM] Seeding initial sample data...");

        Doctor d1 = new Doctor(IdGenerator.getInstance().generateDoctorId(), "Sarah Jenkins", 42, "Female",
                "+1-555-0192", "s.jenkins@meditrack.org", Specialization.CARDIOLOGY, 250.0, true);
        Doctor d2 = new Doctor(IdGenerator.getInstance().generateDoctorId(), "Marcus Vance", 38, "Male",
                "+1-555-0183", "m.vance@meditrack.org", Specialization.NEUROLOGY, 300.0, true);
        Doctor d3 = new Doctor(IdGenerator.getInstance().generateDoctorId(), "Elena Rostova", 35, "Female",
                "+1-555-0144", "e.rostova@meditrack.org", Specialization.DERMATOLOGY, 180.0, true);
        Doctor d4 = new Doctor(IdGenerator.getInstance().generateDoctorId(), "James Wilson", 45, "Male",
                "+1-555-0177", "j.wilson@meditrack.org", Specialization.GENERAL_PHYSICIAN, 120.0, true);

        doctorService.addDoctor(d1);
        doctorService.addDoctor(d2);
        doctorService.addDoctor(d3);
        doctorService.addDoctor(d4);

        Patient p1 = new Patient(IdGenerator.getInstance().generatePatientId(), "John Miller", 34, "Male",
                "+1-555-9011", "john.m@gmail.com", "O+", Arrays.asList("Hypertension"));
        Patient p2 = new Patient(IdGenerator.getInstance().generatePatientId(), "Emily Watson", 29, "Female",
                "+1-555-9022", "emily.w@yahoo.com", "A-", Arrays.asList("Seasonal Asthma"));

        patientService.registerPatient(p1);
        patientService.registerPatient(p2);

        appointmentService.createAppointment(p1, d1, LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), "Routine cardiology check");
        appointmentService.createAppointment(p2, d3, LocalDateTime.now().plusDays(2).withHour(14).withMinute(30), "Skin allergy consultation");
    }

    private void runMenuLoop() {
        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Select an option (1-8): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handlePatientMenu();
                    break;
                case "2":
                    handleDoctorMenu();
                    break;
                case "3":
                    handleAppointmentMenu();
                    break;
                case "4":
                    handleBillingMenu();
                    break;
                case "5":
                    handleAISymptomTriage();
                    break;
                case "6":
                    handlePersistenceMenu();
                    break;
                case "7":
                    runSystemSelfCheck();
                    break;
                case "8":
                    running = false;
                    System.out.println("Thank you for using " + Constants.APP_NAME + ". Goodbye!");
                    appointmentService.shutdown();
                    break;
                default:
                    System.out.println("Invalid selection. Please enter a number between 1 and 8.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=================================================");
        System.out.println("   " + Constants.APP_NAME);
        System.out.println("   Version: " + Constants.APP_VERSION);
        System.out.println("=================================================");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Booking & Scheduling");
        System.out.println("4. Billing & Strategy Calculator");
        System.out.println("5. AI Symptom Triage & Smart Doctor Recommendation");
        System.out.println("6. Data Persistence (CSV & Binary Serialization)");
        System.out.println("7. Run Automated Diagnostics / Self-Check");
        System.out.println("8. Exit Application");
        System.out.println("-------------------------------------------------");
    }

    // --- PATIENT MANAGEMENT MENU ---

    private void handlePatientMenu() {
        System.out.println("\n--- PATIENT MANAGEMENT ---");
        System.out.println("1. Register New Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. Search Patient by ID");
        System.out.println("4. Search Patient by Name (Overloaded)");
        System.out.println("5. Search Patient by Age Range (Overloaded)");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                try {
                    String id = IdGenerator.getInstance().generatePatientId();
                    System.out.print("Enter Full Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Age: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Gender: ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter Phone (+1-XXX-XXXX): ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Blood Group (e.g. O+, A-): ");
                    String blood = scanner.nextLine();

                    Patient p = new Patient(id, name, age, gender, phone, email, blood, new ArrayList<>());
                    patientService.registerPatient(p);
                    System.out.println("[SUCCESS] Registered Patient: " + p.getDetails());
                } catch (Exception e) {
                    System.err.println("[ERROR] Registration failed: " + e.getMessage());
                }
                break;
            case "2":
                System.out.println("\nRegistered Patients:");
                patientService.getAllPatients().forEach(p -> System.out.println(" - " + p.getDetails()));
                break;
            case "3":
                System.out.print("Enter Patient ID: ");
                String pid = scanner.nextLine();
                patientService.searchPatient(pid)
                        .ifPresentOrElse(p -> System.out.println("Found: " + p.getDetails()),
                                () -> System.out.println("Patient not found."));
                break;
            case "4":
                System.out.print("Enter Name string: ");
                String nameQuery = scanner.nextLine();
                List<Patient> byName = patientService.searchPatient(nameQuery, false);
                System.out.println("Search Results (" + byName.size() + " matches):");
                byName.forEach(p -> System.out.println(" - " + p.getDetails()));
                break;
            case "5":
                System.out.print("Enter Min Age: ");
                int minAge = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Max Age: ");
                int maxAge = Integer.parseInt(scanner.nextLine());
                List<Patient> byAge = patientService.searchPatient(minAge, maxAge);
                System.out.println("Age Range Results (" + byAge.size() + " matches):");
                byAge.forEach(p -> System.out.println(" - " + p.getDetails()));
                break;
        }
    }

    // --- DOCTOR MANAGEMENT MENU ---

    private void handleDoctorMenu() {
        System.out.println("\n--- DOCTOR MANAGEMENT ---");
        System.out.println("1. Add New Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. Filter Doctors by Specialization (Streams)");
        System.out.println("4. Sort Doctors by Consultation Fee");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                try {
                    String id = IdGenerator.getInstance().generateDoctorId();
                    System.out.print("Enter Doctor Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Consultation Fee ($): ");
                    double fee = Double.parseDouble(scanner.nextLine());
                    System.out.println("Specializations: CARDIOLOGY, DERMATOLOGY, PEDIATRICS, NEUROLOGY, GENERAL_PHYSICIAN, ORTHOPEDICS");
                    System.out.print("Enter Specialization: ");
                    Specialization spec = Specialization.fromString(scanner.nextLine());

                    Doctor doc = new Doctor(id, name, 40, "Unspecified", "+1-555-0100", name.toLowerCase().replace(" ", "") + "@meditrack.org", spec, fee, true);
                    doctorService.addDoctor(doc);
                    System.out.println("[SUCCESS] Added Doctor: " + doc.getDetails());
                } catch (Exception e) {
                    System.err.println("[ERROR] Doctor addition failed: " + e.getMessage());
                }
                break;
            case "2":
                System.out.println("\nDoctors Directory:");
                doctorService.getAllDoctors().forEach(d -> System.out.println(" - " + d.getDetails()));
                break;
            case "3":
                System.out.print("Enter Specialization name: ");
                Specialization spec = Specialization.fromString(scanner.nextLine());
                List<Doctor> filtered = doctorService.getDoctorsBySpecialization(spec);
                System.out.println("Specialist Results (" + filtered.size() + " matches):");
                filtered.forEach(d -> System.out.println(" - " + d.getDetails()));
                break;
            case "4":
                List<Doctor> sorted = doctorService.getDoctorsSortedByFee(true);
                System.out.println("Doctors sorted by fee (Low to High):");
                sorted.forEach(d -> System.out.println(" - " + d.getDetails()));
                break;
        }
    }

    // --- APPOINTMENT MENU ---

    private void handleAppointmentMenu() {
        System.out.println("\n--- APPOINTMENT BOOKING & SCHEDULING ---");
        System.out.println("1. Book New Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. Cancel Appointment");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                try {
                    System.out.print("Enter Patient ID: ");
                    String pid = scanner.nextLine();
                    Patient patient = patientService.getPatientById(pid)
                            .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + pid));

                    System.out.print("Enter Doctor ID: ");
                    String did = scanner.nextLine();
                    Doctor doctor = doctorService.getDoctorById(did)
                            .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + did));

                    System.out.print("Enter Appointment Date/Time (yyyy-MM-dd HH:mm): ");
                    LocalDateTime dt = DateUtil.parse(scanner.nextLine());

                    System.out.print("Enter Notes/Symptoms: ");
                    String notes = scanner.nextLine();

                    Appointment apt = appointmentService.createAppointment(patient, doctor, dt, notes);
                    System.out.println("[SUCCESS] Appointment Created: " + apt);
                } catch (Exception e) {
                    System.err.println("[ERROR] Booking failed: " + e.getMessage());
                }
                break;
            case "2":
                System.out.println("\nAppointments List:");
                appointmentService.getAllAppointments().forEach(a -> System.out.println(" - " + a));
                break;
            case "3":
                try {
                    System.out.print("Enter Appointment ID to cancel: ");
                    String aptId = scanner.nextLine();
                    appointmentService.cancelAppointment(aptId);
                    System.out.println("[SUCCESS] Appointment " + aptId + " cancelled.");
                } catch (AppointmentNotFoundException e) {
                    System.err.println("[ERROR] " + e.getMessage());
                }
                break;
        }
    }

    // --- BILLING MENU ---

    private void handleBillingMenu() {
        System.out.println("\n--- BILLING & STRATEGY CALCULATOR ---");
        System.out.println("1. Generate Bill for Appointment (Factory Pattern)");
        System.out.println("2. View All Issued Bills & Summaries");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                try {
                    System.out.print("Enter Appointment ID: ");
                    String aptId = scanner.nextLine();
                    System.out.println("Billing Strategy Types: 1. STANDARD, 2. INSURANCE, 3. SENIOR_DISCOUNT");
                    System.out.print("Select Type (1-3): ");
                    String typeChoice = scanner.nextLine();
                    BillFactory.BillType bType = BillFactory.BillType.STANDARD;
                    if ("2".equals(typeChoice)) bType = BillFactory.BillType.INSURANCE;
                    if ("3".equals(typeChoice)) bType = BillFactory.BillType.SENIOR_DISCOUNT;

                    Bill bill = appointmentService.generateBillForAppointment(aptId, bType);
                    System.out.println("[SUCCESS] Bill Issued: " + bill);
                    System.out.println("[IMMUTABLE SNAPSHOT] " + bill.toSummary());
                } catch (Exception e) {
                    System.err.println("[ERROR] Billing failed: " + e.getMessage());
                }
                break;
            case "2":
                System.out.println("\nIssued Bills:");
                appointmentService.getAllBills().forEach(b -> System.out.println(" - " + b));
                break;
        }
    }

    // --- AI SYMPTOM TRIAGE ---

    private void handleAISymptomTriage() {
        System.out.println("\n--- AI SYMPTOM TRIAGE & DOCTOR RECOMMENDATION ENGINE ---");
        System.out.print("Describe patient symptoms (e.g. 'chest pain and shortness of breath'): ");
        String symptoms = scanner.nextLine();

        Specialization spec = AIHelper.analyzeSymptoms(symptoms);
        System.out.println("[AI ANALYSIS] Inferred Medical Category: " + spec.getDisplayName());

        List<Doctor> recommended = AIHelper.recommendDoctors(symptoms, doctorService.getAllDoctors());
        System.out.println("\nRecommended Available Specialists:");
        if (recommended.isEmpty()) {
            System.out.println("No matching available specialists found.");
        } else {
            for (Doctor doc : recommended) {
                System.out.println(" -> " + doc.getDetails());
                List<LocalDateTime> slots = AIHelper.suggestAppointmentSlots(doc, 3);
                System.out.println("    Auto-suggested Next Available Slots:");
                slots.forEach(s -> System.out.println("      * " + DateUtil.format(s)));
            }
        }
    }

    // --- PERSISTENCE MENU ---

    private void handlePersistenceMenu() {
        System.out.println("\n--- DATA PERSISTENCE & FILE I/O ---");
        System.out.println("1. Save Data to CSV Files");
        System.out.println("2. Load Data from CSV Files");
        System.out.println("3. Save DataStore to Binary Serialization (.ser)");
        System.out.println("4. Load DataStore from Binary Serialization (.ser)");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                saveDataToCSV();
                break;
            case "2":
                loadDataFromCSV();
                break;
            case "3":
                try {
                    patientStore.saveToFile(Constants.DATASTORE_SER);
                    System.out.println("[SUCCESS] Saved DataStore binary serialization to " + Constants.DATASTORE_SER);
                } catch (IOException e) {
                    System.err.println("[ERROR] Serialization failed: " + e.getMessage());
                }
                break;
            case "4":
                try {
                    patientStore.loadFromFile(Constants.DATASTORE_SER);
                    System.out.println("[SUCCESS] Restored DataStore binary serialization. Total Patients: " + patientStore.size());
                } catch (Exception e) {
                    System.err.println("[ERROR] Deserialization failed: " + e.getMessage());
                }
                break;
        }
    }

    private void saveDataToCSV() {
        try {
            CSVUtil.savePatientsToCSV(patientService.getAllPatients(), Constants.PATIENTS_CSV);
            CSVUtil.saveDoctorsToCSV(doctorService.getAllDoctors(), Constants.DOCTORS_CSV);
            CSVUtil.saveAppointmentsToCSV(appointmentService.getAllAppointments(), Constants.APPOINTMENTS_CSV);
            System.out.println("[SUCCESS] Successfully saved all Patients, Doctors, and Appointments to CSV files in '" + Constants.DATA_DIR + "'.");
        } catch (IOException e) {
            System.err.println("[ERROR] Saving CSV data failed: " + e.getMessage());
        }
    }

    private void loadDataFromCSV() {
        try {
            List<Patient> patients = CSVUtil.loadPatientsFromCSV(Constants.PATIENTS_CSV);
            patients.forEach(patientService::registerPatient);

            List<Doctor> doctors = CSVUtil.loadDoctorsFromCSV(Constants.DOCTORS_CSV);
            doctors.forEach(doctorService::addDoctor);

            System.out.println("[SUCCESS] Loaded " + patients.size() + " Patients and " + doctors.size() + " Doctors from CSV storage.");
        } catch (IOException e) {
            System.err.println("[ERROR] Loading CSV data failed: " + e.getMessage());
        }
    }

    private void runSystemSelfCheck() {
        System.out.println("\n[SYSTEM] Running Automated Test Suite Diagnostic...");
        com.airtribe.meditrack.test.TestRunner.main(new String[0]);
    }
}
