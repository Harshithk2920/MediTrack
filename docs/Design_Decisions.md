# MediTrack — Design Decisions & Architecture Rationale

## 1. Core OOP Principles Implementation

### A. Encapsulation
- All entity fields in `Person`, `Doctor`, `Patient`, `Appointment`, and `Bill` are declared `private`.
- Access is strictly controlled through public getters and setters backed by centralized validation in `Validator.java`.
- Defensive copying is used when returning collections (e.g., `Collections.unmodifiableList(medicalHistory)` in `Patient`).

### B. Inheritance
- `MedicalEntity` forms the abstract root entity establishing identity (`id`) and audit timestamps (`createdAt`).
- `Person` extends `MedicalEntity` to encapsulate common demographic attributes (name, age, gender, contact details).
- `Doctor` and `Patient` extend `Person`, inheriting state while providing domain-specific behavior.

### C. Polymorphism
- **Method Overriding (Dynamic Dispatch)**: `getDetails()` is declared abstract in `MedicalEntity` and custom-implemented across `Doctor` and `Patient`.
- **Method Overloading (Compile-time Polymorphism)**: `PatientService` implements overloaded `searchPatient()` signatures:
  1. `searchPatient(String id)`
  2. `searchPatient(String name, boolean exactMatch)`
  3. `searchPatient(int minAge, int maxAge)`

### D. Abstraction & Interfaces
- Abstract class `MedicalEntity` defines template contracts.
- Interfaces `Searchable<T>` and `Payable` define behavior contracts.
- Java 8 `default` methods in `Payable` (e.g., `calculateTax(double amount)`) allow centralized tax calculation rules without code duplication.

---

## 2. Advanced OOP Features

### A. Deep vs Shallow Copy Cloning
- `Patient` and `Appointment` implement `java.lang.Cloneable`.
- `Patient.clone()` performs a **Deep Copy** of the mutable `medicalHistory` list, preventing external mutation of the clone's internal state.
- `Appointment.clone()` deeply clones the inner `Patient` object.

### B. Immutability Pattern
- `BillSummary` is designed as an immutable snapshot value object:
  - `final` class declaration prevents subclassing.
  - All fields are `private final`.
  - No setter methods exist.
  - Thread-safe by design for concurrent reporting.

### C. Enums
- `Specialization` enum categorizes doctor fields and embeds symptom keyword lists for AI rule matching.
- `AppointmentStatus` models appointment lifecycle transitions.

---

## 3. Design Patterns Applied

| Pattern | Class / Component | Purpose |
| :--- | :--- | :--- |
| **Singleton** | `IdGenerator`, `DataStore` | Ensures a single global sequence generator and storage manager across the application. Demonstrates both **Eager** and **Lazy (Double-Checked Locking)** initialization. |
| **Strategy** | `BillingStrategy`, `StandardBilling`, `InsuranceBilling`, `DiscountBilling` | Decouples billing calculation logic from the `Bill` entity, allowing flexible, runtime-swappable pricing models. |
| **Factory** | `BillFactory` | Encapsulates the instantiation of `Bill` objects and their associated strategies. |
| **Observer** | `AppointmentSubject`, `ConsoleNotificationObserver` | Decouples appointment lifecycle updates from notification logic (console logging, reminders). |

---

## 4. Concurrency & Synchronization

- `AtomicInteger` in `IdGenerator` ensures unique, thread-safe sequence incrementation without explicit lock overhead.
- `ConcurrentHashMap` and `synchronized` blocks in `DataStore<T>` ensure thread-safe operations under multi-threaded read/write scenarios.
- Background `TimerTask` in `AppointmentService` performs periodic background checks for upcoming appointments.

---

## 5. File Persistence & Exception Handling

- **CSV Persistence**: `CSVUtil.java` uses `try-with-resources` to manage I/O streams safely and `String.split(",")` for line parsing. Supports `--loadData` CLI initialization.
- **Serialization**: Native Java object serialization supported via `DataStore.saveToFile()` and `loadFromFile()`.
- **Custom Exceptions**:
  - `InvalidDataException` (Unchecked) for input validation failures.
  - `AppointmentNotFoundException` (Checked) for handling lookup failures with exception chaining.
