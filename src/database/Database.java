package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Student;
import model.Grade;

public class Database {

    private static final String URL = "jdbc:sqlite:studentmanager.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage()); // System.err statt out
            return null;
        }
    }

    public static void initializeDatabase() {
        String createStudentsTable =
                "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "matricule TEXT NOT NULL UNIQUE, " +
                "name TEXT NOT NULL, " +
                "program TEXT NOT NULL" +
                ");";

        String createGradesTable =
                "CREATE TABLE IF NOT EXISTS grades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "module TEXT NOT NULL, " +
                "grade REAL NOT NULL, " +
                "FOREIGN KEY(student_id) REFERENCES students(id)" +
                ");";

        // try-with-resources: conn und stmt werden automatisch geschlossen
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createStudentsTable);
            stmt.execute(createGradesTable);
            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void addStudent(String matricule, String name, String program) {
        String sql = "INSERT INTO students(matricule, name, program) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);
            pstmt.setString(2, name);
            pstmt.setString(3, program);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (SQLException e) {
            System.err.println("Error inserting student: " + e.getMessage());
        }
    }

    public static void listStudents() {
        String sql = "SELECT * FROM students";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("matricule"),
                    rs.getString("name"),
                    rs.getString("program")
                );
                System.out.println(student); // utilise toString()
            }
            if (!found) System.out.println("No students found.");

        } catch (SQLException e) {
            System.err.println("Error reading students: " + e.getMessage());
        }
    }

    public static void deleteStudent(String matricule) {
        String sql = "DELETE FROM students WHERE matricule = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with this matricule.");
            }

        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    public static void updateStudentProgram(String matricule, String newProgram) {
        String sql = "UPDATE students SET program = ? WHERE matricule = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newProgram);
            pstmt.setString(2, matricule);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("No student found with this matricule.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
    }

    public static void addGrade(String matricule, String module, double grade) {
        String findStudentSql = "SELECT id FROM students WHERE matricule = ?";
        String insertGradeSql = "INSERT INTO grades(student_id, module, grade) VALUES(?, ?, ?)";

        // Zwei verschachtelte try-with-resources weil zwei separate Queries
        try (Connection conn = connect();
             PreparedStatement findStmt = conn.prepareStatement(findStudentSql)) {

            findStmt.setString(1, matricule);

            try (ResultSet rs = findStmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No student found with this matricule.");
                    return;
                }

                int studentId = rs.getInt("id");

                try (PreparedStatement insertStmt = conn.prepareStatement(insertGradeSql)) {
                    insertStmt.setInt(1, studentId);
                    insertStmt.setString(2, module);
                    insertStmt.setDouble(3, grade);
                    insertStmt.executeUpdate();
                    System.out.println("Grade added successfully!");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error adding grade: " + e.getMessage());
        }
    }

    public static void listGrades(String matricule) {
        String sql =
            "SELECT students.name, grades.id, grades.student_id, grades.module, grades.grade " +
            "FROM students " +
            "JOIN grades ON students.id = grades.student_id " +
            "WHERE students.matricule = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    Grade grade = new Grade(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getString("module"),
                        rs.getDouble("grade")
                    );
                    System.out.println(rs.getString("name") + " | " + grade);
                }
                if (!found) System.out.println("No grades found for this student.");
            }

        } catch (SQLException e) {
            System.err.println("Error reading grades: " + e.getMessage());
        }
    }

    public static void calculateAverage(String matricule) {
        String sql =
                "SELECT AVG(grades.grade) AS average_grade " +
                "FROM students " +
                "JOIN grades ON students.id = grades.student_id " +
                "WHERE students.matricule = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);

            try (ResultSet rs = pstmt.executeQuery()) {
                double average = rs.getDouble("average_grade");
                if (rs.wasNull()) {
                    System.out.println("No grades found for this student.");
                } else {
                    System.out.printf("Average grade: %.2f%n", average); // formatierte Ausgabe
                }
            }

        } catch (SQLException e) {
            System.err.println("Error calculating average: " + e.getMessage());
        }
    }
}