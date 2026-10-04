# StudentManager

![Build Status](https://github.com/Bolivar-ng/StudentManager/actions/workflows/build.yml/badge.svg)

A Java application for managing students and grades, with a console interface and a Swing desktop GUI, backed by a SQLite database.

Built as a portfolio project during my Applied Computer Science studies at HTW Berlin.

---

## Features

- Add, list, update and delete students
- Add and list grades per student, with the average grade
- Two interfaces on top of the same service layer: a console menu and a Swing desktop GUI
- Input validation: empty fields, grades limited to the German scale (1.0–5.0), duplicate matricule detection
- Crash-safe console input (invalid numbers are caught and re-prompted)
- Foreign key enforcement with cascading delete: removing a student also removes their grades
- Automatic database initialization on startup
- Automated tests (JUnit 5) and continuous integration (GitHub Actions) on every push

---

## Tech Stack

| Technology | Usage |
|------------|-------|
| Java 25 | Core application logic |
| Swing | Desktop GUI |
| SQLite + JDBC (`sqlite-jdbc`) | Local database |
| Maven | Build and dependency management |
| JUnit 5 | Automated tests |
| GitHub Actions | CI: build and tests on every push and pull request |
| Git & GitHub | Version control |

---

## Architecture

The project is split into layers. Each layer has one responsibility and does not do another layer's job:

| Layer | Responsibility | Never does |
|-------|----------------|------------|
| `Main`, `MainFrame`, `GradesDialog` | User interface: console menu or Swing windows, displaying results | SQL, business rules |
| `StudentService` | Validation, business rules, orchestration | SQL, console or GUI output |
| `Database` | SQL queries only, returns plain data | Output, validation |
| `model` | `Student` and `Grade`: self-validating objects, immutable where possible | |

Concretely:

- `Database` methods return `boolean`, `List<T>`, `Optional<T>` or `OptionalDouble`, never `void` with a `println` inside.
- `StudentService` throws `IllegalArgumentException` for invalid input and lets `SQLException` propagate for real database errors. It never prints anything itself.
- The user interface classes are the only ones that talk to the user. The console and the GUI both call the same `StudentService`; the GUI was added without changing the service or database layers.
- All SQL lives in one class, `Database`.

---

## Tests

`DatabaseTest` contains 6 JUnit 5 tests covering adding a student, duplicate matricule rejection, listing, lookup by matricule and cascading delete of grades. Each test runs against a temporary SQLite file that is created and deleted around it, so the real `studentmanager.db` is never touched.

GitHub Actions runs `mvn verify` (compile and tests) on every push and pull request to `main`.

---

## Project Structure

```text
StudentManager/
├── pom.xml
├── .github/workflows/build.yml      # CI pipeline
└── src/
    ├── main/java/
    │   ├── app/
    │   │   ├── Main.java            # Console entry point
    │   │   ├── MainFrame.java       # Swing GUI entry point
    │   │   └── GradesDialog.java    # Swing dialog: grades and average per student
    │   ├── database/
    │   │   └── Database.java        # SQL queries, returns data
    │   ├── model/
    │   │   ├── Student.java
    │   │   └── Grade.java
    │   └── service/
    │       └── StudentService.java  # Validation and orchestration
    └── test/java/database/
        └── DatabaseTest.java
```

---

## Getting Started

### Prerequisites

- JDK 25 (the version configured in `pom.xml`)
- Maven, or an IDE with Maven support (Eclipse, IntelliJ)

`sqlite-jdbc` is downloaded by Maven automatically.

### Build and test

```bash
git clone https://github.com/Bolivar-ng/StudentManager.git
cd StudentManager
mvn verify
```

### Run

Run one of the two entry points from your IDE:

- `app.Main` for the console interface
- `app.MainFrame` for the Swing GUI

The database file `studentmanager.db` is created automatically on first run, with foreign key constraints enforced and cascading delete on grades.

---

## Planned Features

- `StudentDao` interface, so the persistence layer can be swapped or replaced by a fake in tests
- Unit tests for `StudentService` (currently only the database layer is tested)
- Maven wrapper (`mvnw`) so no local Maven installation is needed
- Predefined programs and modules, selectable from a list
- Export grades to CSV

---

## Author

**Bolivar Nouaze Nguegoh**
Applied Computer Science Student, HTW Berlin
[GitHub](https://github.com/Bolivar-ng)
