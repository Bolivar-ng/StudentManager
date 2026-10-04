# StudentManager
![Build Status](https://github.com/Bolivar-ng/StudentManager/actions/workflows/build.yml/badge.svg)
A Java console application for managing students and grades using a SQLite database.

Built as a portfolio project during my Applied Computer Science studies at HTW Berlin.

---

## Features

- Add, list, update and delete students
- Add and list grades per student
- Calculate average grade per student
- Input validation (empty fields, grade range 0–20, duplicate matricule detection)
- Crash-safe input handling (invalid types are caught and re-prompted)
- Automatic database initialization on startup
- Cascading delete: removing a student also removes their grades

---

## Tech Stack

| Technology | Usage |
|------------|-------|
| Java | Core application logic |
| SQLite | Local database |
| JDBC | Database connection |
| Git & GitHub | Version control |

---

## Architecture

The project follows a strict layered architecture — each layer has one responsibility
and never does another layer's job:

| Layer | Responsibility | Never does |
|-------|----------------|------------|
| `Main` | Console I/O: prompts, menu, displaying results | SQL, validation logic |
| `StudentService` | Validation, business rules, orchestration | SQL, console output |
| `Database` | SQL queries only, returns plain data | Console output, validation |
| `model` | Immutable data objects (`Student`, `Grade`) with self-validating constructors | — |

Concretely:
- `Database` methods return `boolean`, `List<T>`, `Optional<T>` or `OptionalDouble` —
  never `void` with a `println` inside.
- `StudentService` throws `IllegalArgumentException` for invalid input and lets
  `SQLException` propagate for real database errors — it never prints an error itself.
- `Main` is the only class that calls `System.out` / `System.err`.

This separation makes the service layer independently testable (no console coupling)
and keeps the data layer swappable (e.g. a future switch to PostgreSQL would only
touch `Database`).

---

## Project Structure

src/
├── app/
│   └── Main.java          # Entry point, console menu, all output
├── database/
│   └── Database.java      # SQL queries, returns data (no println)
├── model/
│   ├── Student.java        # Self-validating, immutable where possible
│   └── Grade.java
└── service/
    └── StudentService.java # Validation + orchestration, no SQL/no output

---

## Getting Started

### Prerequisites
- Java 11 or higher
- No external dependencies — SQLite driver included via JDBC

### Run the project
\`\`\`bash
git clone https://github.com/Bolivar-ng/StudentManager.git
cd StudentManager
# Compile and run via your IDE (Eclipse, IntelliJ) or command line
\`\`\`

The database file `studentmanager.db` is created automatically on first run
(foreign key constraints are enforced, with cascading delete on grades).

---

## Planned Features

- Maven build (`mvnw` wrapper) + `sqlite-jdbc` as a managed dependency
- JUnit 5 tests against an in-memory SQLite database
- `StudentDao` interface for swappable persistence implementations
- GitHub Actions CI (build + tests on every push)
- Predefined programs/modules selectable from a list
- Export grades to CSV

---

## Author

**Bolivar Nouaze Nguegoh**
Applied Computer Science Student — HTW Berlin
[GitHub](https://github.com/Bolivar-ng)