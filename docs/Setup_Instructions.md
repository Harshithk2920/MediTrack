# MediTrack — Environment Setup & Instructions

## 1. Prerequisites

- **Java Development Kit (JDK)**: JDK 8 or higher (JDK 17/21/25 recommended).
- **Operating System**: Windows / macOS / Linux.
- **Tools**: Terminal / PowerShell / Command Prompt.

---

## 2. Environment Verification

Open your terminal and verify Java installation:

```bash
java -version
javac -version
```

Expected Output Example:
```text
java version "25.0.1" 2025-10-21 LTS
javac 25.0.1
```

---

## 3. Project Directory Structure

```text
MediTrack/
├── src/main/java/com/airtribe/meditrack/
│   ├── Main.java
│   ├── constants/
│   ├── entity/
│   ├── factory/
│   ├── observer/
│   ├── strategy/
│   ├── service/
│   ├── util/
│   ├── exception/
│   ├── interfaces/
│   └── test/
├── docs/
│   ├── JVM_Report.md
│   ├── Setup_Instructions.md
│   └── Design_Decisions.md
└── README.md
```

---

## 4. Compilation & Execution Commands

### A. Compiling the Application

Navigate to the project root directory (`d:/MediTrack`) and compile all Java source files into a `bin/` target directory:

#### PowerShell (Windows):
```powershell
javac -d bin -sourcepath src/main/java (Get-ChildItem -Recurse -Filter *.java src/main/java).FullName
```

#### Bash / Linux / macOS:
```bash
find src/main/java -name "*.java" | xargs javac -d bin -sourcepath src/main/java
```

---

### B. Running the Automated Manual Test Suite

Execute the assertion-based test runner (`TestRunner.java`) to verify all OOP features, cloning, immutability, design patterns, file persistence, and streams:

```bash
java -cp bin com.airtribe.meditrack.test.TestRunner
```

---

### C. Running the Main Application CLI

Launch the interactive console application:

```bash
java -cp bin com.airtribe.meditrack.Main
```

---

### D. Running with `--loadData` CLI Argument

Launch the application and automatically load pre-persisted CSV state:

```bash
java -cp bin com.airtribe.meditrack.Main --loadData
```
