# StudentManager

A Java console application for managing students and grades using a SQLite database.

Built as a portfolio project during my Applied Computer Science studies at HTW Berlin.

---

## Features

- Add, list, update and delete students
- Add and list grades per student
- Calculate average grade per student
- Input validation (empty fields, grade range 0–20)
- Crash-safe input handling (invalid types are caught and re-prompted)
- Automatic database initialization on startup

---

## Tech Stack

| Technology | Usage |
|------------|-------|
| Java | Core application logic |
| SQLite | Local database |
| JDBC | Database connection |
| Git & GitHub | Version control |

---

## Project Structure  
src/
├── app/
│   └── Main.java          # Entry point, console menu, input handling
├── database/
│   └── Database.java      # All SQL operations (CRUD)
├── model/
│   ├── Student.java       # Student data model
│   └── Grade.java         # Grade data model
└── service/
└── StudentService.java # Business logic and input validation
---

## Architecture 
Main → StudentService → Database → SQLite 
The project follows a layered architecture:
- **app** — user interface and input handling
- **service** — validation and business logic
- **database** — SQL queries and connection management
- **model** — data objects (Student, Grade)

---

## Getting Started

### Prerequisites
- Java 11 or higher
- No external dependencies — SQLite driver included via JDBC

### Run the project
```bash
git clone https://github.com/Bolivar-ng/StudentManager.git
cd StudentManager
# Compile and run via your IDE (Eclipse, IntelliJ) or command line
```

The database file `studentmanager.db` is created automatically on first run.

---

## Planned Features

- Predefined programs and modules (selectable from a list)
- Export grades to CSV
- JUnit tests for service layer

---

## Author

**Bolivar Nouaze Nguegoh**  
Applied Computer Science Student — HTW Berlin  
[GitHub](https://github.com/Bolivar-ng)
