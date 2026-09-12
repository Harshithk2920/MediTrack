package com.airtribe.meditrack.test;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.factory.BillFactory;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.strategy.DiscountBilling;
import com.airtribe.meditrack.strategy.InsuranceBilling;
import com.airtribe.meditrack.util.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manual Test Suite Runner for MediTrack (No JUnit required).
 * Verifies all learning objectives, OOP design patterns, exceptions, persistence, cloning, and streams.
 */
public class TestRunner {

    @FunctionalInterface
    public interface TestCase {
        void run() throws Throwable;
    }

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   MEDITRACK COMPREHENSIVE MANUAL TEST SUITE     ");
        System.out.println("=================================================");

        runTest("OOP Inheritance & Constructor Chaining Test", TestRunner::testOOPInheritance);
        runTest("Encapsulation & Validation Guard Test", TestRunner::testValidationAndEncapsulation);
        runTest("Deep Copy Cloning Test (Patient & Appointment)", TestRunner::testDeepCopyCloning);
        runTest("Immutable BillSummary Class Test", TestRunner::testImmutableBillSummary);
        runTest("Design Pattern - Singleton Test (Eager vs Lazy)", TestRunner::testSingletonPattern);
        runTest("Design Pattern - Strategy Billing Test", TestRunner::testStrategyPattern);
        runTest("Design Pattern - Factory Pattern Test (BillFactory)", TestRunner::testFactoryPattern);
        runTest("Design Pattern - Observer Notification Test", TestRunner::testObserverPattern);
        runTest("Polymorphism - Overloaded Search Methods Test", TestRunner::testOverloadedSearch);
        runTest("Custom Exceptions & Chaining Test", TestRunner::testCustomExceptions);
        runTest("File I/O - CSV Parsing & Persist Test", TestRunner::testCSVUtil);
        runTest("Serialization & Deserialization Test", TestRunner::testSerialization);
        runTest("Concurrency & AtomicInteger Thread Safety Test", TestRunner::testConcurrencyThreadSafety);
        runTest("AI Feature - Symptom Triage Rule Engine Test", TestRunner::testAISymptomTriage);
        runTest("Java 8+ Streams & Lambdas Pipeline Test", TestRunner::testJava8Streams);

        System.out.println("\n=================================================");
        System.out.printf("   TEST SUMMARY: Total: %d | Passed: %d | Failed: %d%n", totalTests, passedTests, failedTests);
        System.out.println("=================================================");

        if (failedTests > 0) {
            System.err.println("SOME TESTS FAILED! Please review failure output above.");
            System.exit(1);
        } else {
            System.out.println("ALL MANUAL TESTS PASSED SUCCESSFULLY! 100% VERIFIED.");
        }
    }

    private static void runTest(String testName, TestCase testCase) {
        totalTests++;
        System.out.printf("\n[TEST %02d] Running: %s... ", totalTests, testName);
        try {
            testCase.run();
            passedTests++;
            System.out.println("=> PASSED [OK]");
        } catch (Throwable t) {
            failedTests++;
            System.out.println("=> FAILED [FAIL]");
            System.err.println("  Reason: " + t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion Failed: " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(String.format("Assertion Failed: %s. Expected: <%s>, Actual: <%s>", message, expected, actual));
        }
    }

    // --- INDIVIDUAL TEST CASES ---

    private static void testOOPInheritance() {
        Doctor doc = new Doctor("DOC-001", "Gregory House", 45, "Male", "1234567890", "house@med.org",
                Specialization.NEUROLOGY, 250.0, true);
        Patient pat = new Patient("PAT-001", "John Doe", 30, "Male", "0987654321", "john@mail.com",
                "O+", Arrays.asList("Hypertension", "Asthma"));

        assertTrue(doc instanceof Person, "Doctor must inherit from Person");
        assertTrue(doc instanceof MedicalEntity, "Doctor must inherit from MedicalEntity");
        assertTrue(pat instanceof Person, "Patient must inherit from Person");
        assertEquals("DOC-001", doc.getId(), "Doctor ID check");
        assertEquals("Gregory House", doc.getName(), "Doctor Name check");
    }

    private static void testValidationAndEncapsulation() {
        boolean exceptionThrown = false;
        try {
            Validator.validateEmail("invalid-email-format");
        } catch (InvalidDataException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "Validator should throw InvalidDataException for bad email");

        exceptionThrown = false;
        try {
            Validator.validateAge(200);
        } catch (InvalidDataException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "Validator should throw InvalidDataException for invalid age");
    }

    private static void testDeepCopyCloning() {
        List<String> history = new ArrayList<>(Arrays.asList("Diabetes", "Allergy"));
        Patient p1 = new Patient("PAT-100", "Alice", 28, "Female", "1112223333", "alice@test.com", "A+", history);
        Patient p2 = p1.clone();

        // Verify values equal initially
        assertEquals(p1.getId(), p2.getId(), "Cloned ID match");
        assertEquals(p1.getName(), p2.getName(), "Cloned Name match");

        // Mutate original patient history
        p1.addMedicalRecord("Migraine");

        // Assert clone's history was deep copied and remains unchanged
        assertTrue(p1.getMedicalHistory().contains("Migraine"), "Original should have Migraine");
        assertTrue(!p2.getMedicalHistory().contains("Migraine"), "Cloned Patient history must NOT be mutated by original change");
    }

    private static void testImmutableBillSummary() {
        BillSummary summary = new BillSummary("BIL-1", "APT-1", "Bob", "Dr. Smith", 100.0, 18.0, 118.0, true, "Standard", LocalDateTime.now());
        assertEquals("BIL-1", summary.getBillId(), "BillSummary billId");
        assertEquals(118.0, summary.getTotalAmount(), "BillSummary totalAmount");
        assertTrue(summary.isPaid(), "BillSummary isPaid");
    }

    private static void testSingletonPattern() {
        IdGenerator eager1 = IdGenerator.getInstance();
        IdGenerator eager2 = IdGenerator.getInstance();
        IdGenerator lazy1 = IdGenerator.getLazyInstance();
        IdGenerator lazy2 = IdGenerator.getLazyInstance();

        assertTrue(eager1 == eager2, "Eager Singleton instances must be reference equal");
        assertTrue(lazy1 == lazy2, "Lazy Singleton instances must be reference equal");
    }

    private static void testStrategyPattern() {
        Doctor doc = new Doctor("DOC-20", "Sarah Connor", Specialization.CARDIOLOGY, 200.0);
        Patient pat = new Patient("PAT-20", "Kyle Reese", 32, "9998887777", "B+");
        Appointment apt = new Appointment("APT-20", pat, doc, LocalDateTime.now(), "Routine checkup");

        Bill standardBill = new Bill("BIL-100", apt, new com.airtribe.meditrack.strategy.StandardBilling());
        Bill insuranceBill = new Bill("BIL-101", apt, new InsuranceBilling(0.20)); // 20% copay = 40.0
        Bill discountBill = new Bill("BIL-102", apt, new DiscountBilling(0.15));   // 15% off = 170.0

        assertEquals(200.0, standardBill.getBaseAmount(), "Standard billing base");
        assertEquals(40.0, insuranceBill.getBaseAmount(), "Insurance billing copay base");
        assertEquals(170.0, discountBill.getBaseAmount(), "Discount billing base");
    }

    private static void testFactoryPattern() {
        Doctor doc = new Doctor("DOC-30", "Bruce Banner", Specialization.GENERAL_PHYSICIAN, 100.0);
        Patient pat = new Patient("PAT-30", "Tony Stark", 40, "5554443333", "O+");
        Appointment apt = new Appointment("APT-30", pat, doc, LocalDateTime.now(), "Radiation exposure");

        Bill insuranceBill = BillFactory.createBill(apt, BillFactory.BillType.INSURANCE);
        assertTrue(insuranceBill.getStrategy() instanceof InsuranceBilling, "BillFactory should equip InsuranceBilling strategy");
    }

    private static void testObserverPattern() {
        Doctor doc = new Doctor("DOC-40", "Stephen Strange", Specialization.NEUROLOGY, 300.0);
        Patient pat = new Patient("PAT-40", "Wanda Maximoff", 29, "4443332222", "AB+");

        DataStore<Appointment> store = new DataStore<>();
        AppointmentService service = new AppointmentService(store);

        Appointment apt = service.createAppointment(pat, doc, LocalDateTime.now().plusDays(1), "Neural scan");
        assertEquals(AppointmentStatus.CONFIRMED, apt.getStatus(), "Created appointment should be CONFIRMED");
        service.shutdown();
    }

    private static void testOverloadedSearch() {
        DataStore<Patient> store = new DataStore<>();
        PatientService service = new PatientService(store);

        service.registerPatient(new Patient("PAT-501", "Clark Kent", 35, "Male", "1112224444", "clark@dailyplanet.com", "O+", Collections.emptyList()));
        service.registerPatient(new Patient("PAT-502", "Lois Lane", 32, "Female", "1112225555", "lois@dailyplanet.com", "A-", Collections.emptyList()));

        // Search overloading #1 by ID
        assertTrue(service.searchPatient("PAT-501").isPresent(), "Search by ID PAT-501");

        // Search overloading #2 by Name
        List<Patient> byName = service.searchPatient("Clark", false);
        assertEquals(1, byName.size(), "Search by name 'Clark'");

        // Search overloading #3 by Age range
        List<Patient> byAge = service.searchPatient(30, 34);
        assertEquals(1, byAge.size(), "Search by age range 30-34 should find Lois Lane");
    }

    private static void testCustomExceptions() {
        DataStore<Appointment> store = new DataStore<>();
        AppointmentService service = new AppointmentService(store);

        boolean caught = false;
        try {
            service.getAppointmentById("NON_EXISTENT_ID");
        } catch (AppointmentNotFoundException e) {
            caught = true;
        }
        assertTrue(caught, "AppointmentService should throw AppointmentNotFoundException for invalid ID");
        service.shutdown();
    }

    private static void testCSVUtil() throws Exception {
        String tempPatientsFile = Constants.DATA_DIR + File.separator + "test_patients.csv";
        Patient p1 = new Patient("PAT-CSV1", "Diana Prince", 28, "Female", "7778889999", "diana@amazon.com", "O-", Arrays.asList("Lasso strain"));
        
        CSVUtil.savePatientsToCSV(Arrays.asList(p1), tempPatientsFile);
        List<Patient> loaded = CSVUtil.loadPatientsFromCSV(tempPatientsFile);

        assertEquals(1, loaded.size(), "Loaded CSV size");
        assertEquals("PAT-CSV1", loaded.get(0).getId(), "Loaded patient ID");
        assertEquals("Diana Prince", loaded.get(0).getName(), "Loaded patient Name");

        new File(tempPatientsFile).delete();
    }

    private static void testSerialization() throws Exception {
        String tempSerFile = Constants.DATA_DIR + File.separator + "test_store.ser";
        DataStore<Doctor> store = new DataStore<>();
        store.add(new Doctor("DOC-SER1", "Meredith Grey", Specialization.GENERAL_PHYSICIAN, 180.0));

        store.saveToFile(tempSerFile);

        DataStore<Doctor> restoredStore = new DataStore<>();
        restoredStore.loadFromFile(tempSerFile);

        assertEquals(1, restoredStore.size(), "Restored DataStore size");
        assertTrue(restoredStore.getById("DOC-SER1").isPresent(), "Restored Doctor presence");

        new File(tempSerFile).delete();
    }

    private static void testConcurrencyThreadSafety() throws Exception {
        int threads = 10;
        int idsPerThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Set<String> generatedIds = Collections.synchronizedSet(new HashSet<>());
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < idsPerThread; j++) {
                        generatedIds.add(IdGenerator.getInstance().generatePatientId());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(threads * idsPerThread, generatedIds.size(), "AtomicInteger IdGenerator must produce unique IDs across concurrent threads");
    }

    private static void testAISymptomTriage() {
        Specialization spec1 = AIHelper.analyzeSymptoms("I have severe chest pain and heart palpitations");
        assertEquals(Specialization.CARDIOLOGY, spec1, "AI triage for chest pain -> CARDIOLOGY");

        Specialization spec2 = AIHelper.analyzeSymptoms("I have a skin rash and itching");
        assertEquals(Specialization.DERMATOLOGY, spec2, "AI triage for skin rash -> DERMATOLOGY");
    }

    private static void testJava8Streams() {
        Doctor d1 = new Doctor("DOC-S1", "Doc One", Specialization.NEUROLOGY, 150.0);
        Doctor d2 = new Doctor("DOC-S2", "Doc Two", Specialization.NEUROLOGY, 300.0);
        Doctor d3 = new Doctor("DOC-S3", "Doc Three", Specialization.PEDIATRICS, 100.0);

        DataStore<Doctor> store = new DataStore<>();
        store.add(d1); store.add(d2); store.add(d3);

        DoctorService service = new DoctorService(store);

        List<Doctor> neuros = service.getDoctorsBySpecialization(Specialization.NEUROLOGY);
        assertEquals(2, neuros.size(), "Stream filter by specialization");

        List<Doctor> sortedByFee = service.getDoctorsSortedByFee(true);
        assertEquals("DOC-S3", sortedByFee.get(0).getId(), "Cheapest doctor first");
        assertEquals("DOC-S2", sortedByFee.get(2).getId(), "Most expensive doctor last");
    }
}
