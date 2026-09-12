# MediTrack — Clinic & Appointment Management System

MediTrack is a modular, production-grade Core Java application modeling a Clinic & Appointment Management System. The system models patients, doctors, appointments, and billing; demonstrating strong object-oriented design, SOLID principles, standard Java features (collections, exceptions, file I/O, serialization), and advanced features (concurrency, design patterns, streams, deep cloning).

---

## 🌟 Key Features & Learning Objectives Checklist

- [x] **JVM Architecture & Setup**: Documented in `docs/JVM_Report.md` and `docs/Setup_Instructions.md`.
- [x] **Core OOP Implementation**:
  - Encapsulation with private fields and `Validator` guards.
  - Inheritance (`MedicalEntity` -> `Person` -> `Doctor` / `Patient`).
  - Polymorphism (Overloaded `searchPatient()`, Overridden `getDetails()`).
  - Abstraction & Interfaces (`Searchable<T>`, `Payable` with default tax methods).
- [x] **Advanced OOP Additions**:
  - Deep Copy Cloning (`Patient.clone()` & `Appointment.clone()`).
  - Immutable Class (`BillSummary`).
  - Enums (`Specialization`, `AppointmentStatus`).
  - Static Initialization Blocks in `Constants` and `IdGenerator`.
- [x] **Design Patterns**:
  - **Singleton**: Eager & Lazy Double-Checked Locking in `IdGenerator`.
  - **Strategy**: `StandardBilling`, `InsuranceBilling`, `DiscountBilling`.
  - **Factory**: `BillFactory`.
  - **Observer**: `AppointmentSubject` & `ConsoleNotificationObserver`.
- [x] **Bonus Features (All 4 Implemented)**:
  - **A. File I/O & Persistence**: CSV parsing (`String.split(",")`) with `try-with-resources` & `--loadData` CLI flag.
  - **B. Design Patterns**: Singleton, Factory, Strategy, Observer.
  - **C. AI Feature**: Rule-based symptom triage & slot auto-suggestion in `AIHelper`.
  - **D. Java Streams + Lambdas**: Sorting, filtering, mapping across services.
- [x] **Testing & Verification**: Comprehensive manual test runner in `TestRunner.java`.

---

## 📁 Expected Directory Structure

```text
d:/MediTrack/
├── src/main/java/com/airtribe/meditrack/
│   ├── Main.java                        // Interactive Console CLI Entry Point (--loadData)
│   ├── constants/
│   │   └── Constants.java               // Global Constants (tax rate, file paths)
│   ├── entity/
│   │   ├── MedicalEntity.java           // Abstract base entity
│   │   ├── Person.java                  // Abstract class (extends MedicalEntity)
│   │   ├── Doctor.java                  // Extends Person, implements Searchable
│   │   ├── Patient.java                 // Extends Person, implements Cloneable & Searchable
│   │   ├── Specialization.java          // Enum with symptom keywords
│   │   ├── AppointmentStatus.java       // Enum (PENDING, CONFIRMED, CANCELLED, COMPLETED)
│   │   ├── Appointment.java             // Implements Cloneable (Deep Copy)
│   │   ├── Bill.java                    // Implements Payable, integrates Strategy
│   │   └── BillSummary.java             // Immutable class (thread-safe snapshot)
│   ├── factory/
│   │   └── BillFactory.java             // Factory Pattern for bill creation
│   ├── observer/
│   │   ├── AppointmentSubject.java      // Observer pattern Subject
│   │   ├── AppointmentObserver.java     // Observer interface
│   │   └── ConsoleNotificationObserver.java // Concrete console notification observer
│   ├── strategy/
│   │   ├── BillingStrategy.java         // Strategy interface
│   │   ├── StandardBilling.java         // Full rate strategy
│   │   ├── InsuranceBilling.java        // Copay strategy
│   │   └── DiscountBilling.java         // Discount strategy
│   ├── service/
│   │   ├── DoctorService.java           // Doctor management & Stream queries
│   │   ├── PatientService.java          // Patient management & Overloaded searches
│   │   └── AppointmentService.java      // Appointment booking, cancellation & timer tasks
│   ├── util/
│   │   ├── Validator.java               // Centralized validation logic
│   │   ├── DateUtil.java                // LocalDateTime formatting & parsing
│   │   ├── CSVUtil.java                 // File I/O (try-with-resources & split(","))
│   │   ├── IdGenerator.java             // AtomicInteger Eager & Lazy Singleton
│   │   ├── DataStore.java               // Generic DataStore<T> with serialization
│   │   └── AIHelper.java                // AI symptom triage rule engine
│   ├── exception/
│   │   ├── AppointmentNotFoundException.java // Custom checked exception
│   │   └── InvalidDataException.java        // Custom unchecked exception
│   ├── interfaces/
│   │   ├── Searchable.java              // Generic interface with default methods
│   │   └── Payable.java                 // Payable interface with default tax method
│   └── test/
│       └── TestRunner.java              // Automated manual test suite
├── docs/
│   ├── JVM_Report.md                    // Comprehensive JVM internals report
│   ├── Setup_Instructions.md            // Compilation and execution guide
│   └── Design_Decisions.md              // OOP architecture and design pattern rationale
└── README.md
```

---

## 🚀 Quick Start Commands

### 1. Compile the Project
```powershell
javac -d bin -sourcepath src/main/java (Get-ChildItem -Recurse -Filter *.java src/main/java).FullName
```

### 2. Run All Manual Tests
```powershell
java -cp bin com.airtribe.meditrack.test.TestRunner
```

### 3. Run Main CLI Application
```powershell
java -cp bin com.airtribe.meditrack.Main
```

### 4. Run Main Application with `--loadData` CLI Flag
```powershell
java -cp bin com.airtribe.meditrack.Main --loadData
```
